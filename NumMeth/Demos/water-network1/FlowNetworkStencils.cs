//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
//
// A library of flow components, each coded as a variant f the 'fancy' base class.
// Resistors and conductors are so simple they skip the base class.



using System;
using System.IO;
using System.Text;
using System.Diagnostics;

namespace FlowNetworkStencils // aka Templates
{
public class FNS
    {

      // Resistors need no further beaviour beyond installing in the initial conductance matrix.
      public static void add_resistor(double [] [] GG, int from, int to, double resistance)
      {
	double conductance = 1.0 / resistance;
	if (from<0 && to<0) Debug.Fail("irrelevant conductor added");
	if (to>=0)
	{
	   GG[to][to]       += conductance;
	}
	if (from>=0) 
	{
	   GG[from][from]   += conductance;
	}
	if (from>=0 && to >= 0)
	{
	   GG[from][to]   -= conductance;
	   GG[to][from]   -= conductance;
        }
      }


      public class fancy  // More-exotic components, base class.
      {
          public double old_flow, old_cond; 
          public string name;
	  public double [] [] GG; // Conductance matrix.
	  public double [] rhs;   // Right-hand side (flow sources or 0.0).
	  public int from, to;    // Connected nodes (-1 for ref plane).
	  public fancy next;      // Linked list of all fancies.
          public double [] v;
	  public fancy(fancy a_next,
                       double [] [] a_GG, double [] a_rhs, double [] a_v, 
		       int a_from, int a_to,
		       string a_name)
	  {
	    GG = a_GG; from = a_from; to = a_to; rhs = a_rhs; v = a_v;
	    name = a_name; next = a_next;
	  }

	public virtual bool steady_state_iterate(double deltaT)
	 {
           if (next != null) return next.steady_state_iterate(deltaT);
	   return true;   // Return true if have acceptably converged in current timestep.
         }

	public virtual void time_step_commit(double deltaT)
	 {
           if (next != null) next.time_step_commit(deltaT);
         }


	 public void remove_old_update_from_matrices()
	 {
           if (from >= 0)
           {
              rhs[from] -= old_flow;
              GG[from][from] -= old_cond;
	   }						
  
           if (to >= 0) 
	   {
	      rhs[to] -= old_flow;
              GG[to][to] -= old_cond;  
           }

           if (to >= 0 && from >= 0) 
	   {
              GG[to][from] += old_cond;  
              GG[from][to] += old_cond;  
           }
        }	   

        public void install_update_to_matrices(double cond, double flow)
        { 
          if (from >= 0)
           {
              rhs[from] += flow;
              GG[from][from] += cond;
	   }						
  
           if (to >= 0) 
	   {
	      rhs[to] += flow;
              GG[to][to] += cond;  
           }

           if (to >= 0 && from >= 0) 
	   {
              GG[to][from] -= cond;  
              GG[from][to] -= cond;  
           }
          old_cond = cond; old_flow = flow;
        }	   


       }  

       public class bucket: fancy
       {
	 double capacitance; // The bucket size - prismatic area.
         double SV_voltage;    // The state variable - fill depth (voltage).

	 // Constructor:
	 public bucket(fancy a_next, double [] [] a_GG, double [] a_rhs, double [] a_v, int a_from, int a_to, string a_name, double a_capacitance) : base(a_next, a_GG, a_rhs, a_v, a_from, a_to, a_name)
	 {
	    capacitance = a_capacitance; 
	 }

         public override bool steady_state_iterate(double deltaT)
         {
           // First we remove the previous entries
	   remove_old_update_from_matrices();
	   double cct_voltage = (base.from >= 0) ? base.v[base.from]: 0.0;
	   cct_voltage += (base.to >= 0) ? -base.v[base.to]: 0.0;

   	   double conductance_G_c = capacitance / deltaT; 
	   double flow_I_c = conductance_G_c * SV_voltage;
	   install_update_to_matrices(conductance_G_c, flow_I_c);

	   // This model always converges.
	   bool converged = true;
	   // Now run any further fancy components.
	    
           return (base.steady_state_iterate(deltaT)) && converged;  
         }

         public override void time_step_commit(double deltaT)
         {
           // Now compute bucket (capacitor) inertial, time-varying effect.
	   // Apply forward-difference stencil to state variable.
           // Change in depth/voltage is time step * net flow in or out / capacitance.
	   double cct_voltage = (base.from >= 0) ? base.v[base.from]: 0.0;
	   cct_voltage += (base.to >= 0) ? -base.v[base.to]: 0.0;
	   SV_voltage = cct_voltage;

	   base.time_step_commit(deltaT);
	 }
       }



// Warning: This diode model has not been tested.
       public class diode: fancy
       {
	 double V_d_old; 	 // Keep a note of our previous voltage for convergence indication.

	 // Constructor:
	 public diode(fancy a_next, double [] [] a_GG, double [] a_rhs, double [] a_v, int a_from, int a_to, string a_name) : base(a_next, a_GG, a_rhs, a_v, a_from, a_to, a_name)
	 { }

         public override bool steady_state_iterate(double deltaT)
         {
           // First we remove the previous entries
	   remove_old_update_from_matrices();


	   double V_d = (base.from >= 0) ? base.v[base.from]: 0.0;
	   V_d += (base.to >= 0) ? -base.v[base.to]: 0.0;

	   double conductance_G_d =  10e-12 / 0.026 * Math.Exp(V_d / 0.026); // Shockley diode model
	   conductance_G_d += 10.0; // Add on 10 ohms to help convergence - a little fudge.
	   double flow_I_d = conductance_G_d * V_d; // Current offset to linearise the model: not the current in the diode.
	   install_update_to_matrices(conductance_G_d, flow_I_d);

	   // This model is non-linear and iterations are needed to find the global operating point in the current time step.
	   bool converged = V_d==0.0? true: Math.Abs(V_d - V_d_old) / V_d < 0.001;  
 	   // Now run any further fancy components.  We want to iterate on them too, even if we are not converged, so order is important in the following conjunction.

	     V_d_old = V_d;
           return (base.steady_state_iterate(deltaT)) && converged;  
         }


       } 
   }
}

// eof
