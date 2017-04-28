//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
//
// Linear FDTD simulations in one dimension.


//
// 1. FDTD simulation of heat conduction or RC network (no inductance)
// 2. FDTD simulation of a violin string - has velocity and position in state vector.
//

using System;
using System.IO;
using System.Text;


class RC_heaflow_1d_FDTD
{

  double deltaT = 1;   // Elemental time step
  double sigma = 0.25;      // Conductivity between elements

  const int n_elements = 110;
  const int hscale = 6;
  const int height = 500;

  // State Variables - just temperature (or voltage)
  double [] temp_or_potential = new double [n_elements];

  // Auxiliary vectors.
  double [] new_temp_or_potential = new double [n_elements];
  uint [] plotted_y = new uint [n_elements];

  public int RunFDTD(uint sim_steps)
  {
    Console.WriteLine("Start RC/Heatflow Simulation");
    bool plotting = true;

    PlotUtil.Plotter plotter = new PlotUtil.Plotter();
    plotter.Open(n_elements * hscale, height);

    double time = 0.0;

    // Add an initial impulse
    if (true)
    {
      int o = n_elements/4, w = n_elements/8, ss = height/3;
      for (int x=o-w; x<o+w; x++) temp_or_potential[x] += 1 * (ss-ss*Math.Abs(x-o)/w);
    }

     // Add a second initial impulse
     if (false)
     {
       int o = 300, ss = 80;
       for (int x=o-ss; x<o+ss; x++) temp_or_potential[x] += 3 * (ss-Math.Abs(x-o));
     }

     plotter.MakeXAxis(height/2);
     for (int it=0; it<sim_steps; it++)
       {
	 time += deltaT;
	 for (int x=0; x<n_elements; x++) 
	   {
	     double dex = - 2 * temp_or_potential[x];
	     if (x > 0) dex += temp_or_potential[x-1];
	     if (x < n_elements-1) dex += temp_or_potential[x+1];
	     new_temp_or_potential[x] = temp_or_potential[x] + sigma * deltaT * dex;
	   }
	 double op_tap = temp_or_potential[64];  // Output tap point for t/d plot.
	 if (plotting)  // Do both a time-domain plot and a physical animation.
	   { 
	     for (int x=0; x<n_elements; x++)
	       {
		 uint y    = (uint)(new_temp_or_potential[x]) + height/2;
		 if (y != plotted_y[x])
		   {
		     if (plotted_y[x] != 0) plotter.Set_pixel((uint)(x*hscale), plotted_y[x], 1); // Clear old pixel.
		     plotter.Set_pixel((uint)(x*hscale), y, 2); // Plot new pixel
		     plotted_y[x] = y;
		   }
		 plotter.Set_pixel((uint)(it/4), (uint)(op_tap*4+100), 3); // time domain plot
	       }
	   }
	 for (int x=0; x<n_elements; x++) temp_or_potential[x] = new_temp_or_potential[x];
	 //    for (int x=0; x<n_elements; x++) Console.WriteLine(" {0} {1}", x, old_temp_or_potential[x]);
	 
     }
     return 0;
  }
}


class TestBench
{
  public static int Main()
  {
    uint sim_steps = 2000 * 500; 
    RC_heaflow_1d_FDTD sim = new RC_heaflow_1d_FDTD();
    sim.RunFDTD(sim_steps);
    return 0;
  }
}

//eof

