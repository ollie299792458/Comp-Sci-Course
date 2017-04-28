//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
//
// Linear FDTD simulations in one dimension.


// First-order system:
// 1. FDTD simulation of heat conduction or RC network (no inductance)
//
// Second-order system:
// 2. FDTD simulation of a violin string - has velocity and position in state vector.
//

using System;
using System.IO;
using System.Text;


class ViolinStringFDTD
{

  const int n_elements = 110;
  const int hscale = 6;
  const int height = 500;

  // State Variables
  double [] pos = new double [n_elements];
  double [] vel = new double [n_elements];

  // Auxiliary vectors.
  uint [] plotted_y = new uint [n_elements];


  const double deltaT = 1.0;
  const double rho = 1.0;  // Conductivity.
  
  public void RunFDTD(uint sim_steps)
  {
    double time = 0.0;
    Console.WriteLine("Start String Simulation");
    bool plotting = true;

    //PlotUtil.WavFile wf = new WavFile(sim_steps, 8);
    // wf.write_hdr("Violing.wav");
     //    wf.write_test();
    PlotUtil.Plotter plotter = new PlotUtil.Plotter();
    plotter.Open(n_elements * hscale, height);

    // Add an initial impulse
    if (true)
    {
      int o = n_elements/2, ss = n_elements/3;
      for (int x=o-ss; x<o+ss; x++)    pos[x] += 1 * (ss-Math.Abs(x-o));
    }

     // Add a second initial impulse
     if (false)
     {
       int o = 300, ss = 80;
       for (int x=o-ss; x<o+ss; x++)    pos[x] += 3 * (ss-Math.Abs(x-o));
     }

     plotter.MakeXAxis(height/2);
     for (int it=0; it<sim_steps; it++)
       {
	 time += deltaT;
         for (int x=1; x<n_elements-1; x++) pos[x] += vel[x] * deltaT;
         for (int x=1; x<n_elements-1; x++) 
	   {
	     vel[x] +=  (pos[x-1] + pos [x+1] - 2 * pos[x]) * rho * deltaT;
	     vel[x] *= 0.995;  // Add a little resistive loss.
	   }
	 double op_tap = pos[64];  // Output tap point for t/d plot.
	 if (plotting)  // Do both a time-domain plot and a physical animation.
	   { 
	     for (int x=0; x<n_elements; x++)
	       {
		 uint y = (uint)(pos[x]) + height/2;
		 if (y != plotted_y[x])
		   {
		     if (plotted_y[x] != 0) plotter.Set_pixel((uint)(x*hscale), plotted_y[x], 1); // Clear old pixel.
		     plotter.Set_pixel((uint)(x*hscale), y, 2); // Plot new pixel
		     plotted_y[x] = y;
		   }
		 plotter.Set_pixel((uint)(it/4), (uint)(op_tap*4+100), 3); // time domain plot
	       }
	   }
      }
   //wf.write_val(op >> 0);
    //wf.Close();
  }
}


class TestBench
{
  public static int Main()
  {
    uint sim_steps = 2000 * 500; 
    ViolinStringFDTD sim = new ViolinStringFDTD();
    sim.RunFDTD(sim_steps);
    return 0;
  }
}

//eof

