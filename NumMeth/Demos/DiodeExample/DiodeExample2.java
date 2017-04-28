// University of Cambridge Computer Laboratory - Numerical Methods Demos.
// (C) DJ Greaves, 2014-15.  
//
// DiodeExample2.
//
//
public class DiodeExample2 // Non-linear component simulation in Spice style.
{

    public static void main (String [] args)
    {
	double [] v = { 0.0, 0.0 };
	for(int it=0;it<5000;it++) // We do lots of iterations here, but we are being naive to show the point.
	    {
		double V_d = v[1];
		double G_d =  10e-12 / 0.026 * Math.exp(V_d / 0.026); // Shockley diode model

		G_d += 10.0; // Add on 10 ohms to help convergence - a little fudge.

		double I_d = G_d * V_d; // Current offset to linearise the model: not the current in the diode.

		if ((it % 200) == 0)
		    {
			double actual_diode_i = (v[0]-v[1])/1000.0;
			System.out.printf("Diode2 iteration=%d   v_1=%f v_2=%f  (I_d=%e) I=%e\n", it, v[0], v[1], I_d, actual_diode_i);
		    }
		double [] [] G = {{0.011, -0.001}, {-0.001, 0.001+G_d}};
		double [] rhs = {0.1, I_d};
		double [] v_primed = SimuSolve.Solve(G, rhs);

		v = v_primed;
		// Much better to use Newton Raphson, but we cannot go into that detail 
		// in the available time.
	    }

	System.out.printf("Finished\n");
    }

}

// eof





