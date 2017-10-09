//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.


// Water Network 1 - Ad Hoc Coding, without using a matrix form
// Later we will see how to code this up in flow matrix form.


using System;
using System.IO;
using System.Text;


class WaterNetwork1
{
  const int width = 800, height = 600;
  PlotUtil.Plotter plotter = new PlotUtil.Plotter();


  const double deltaT = 0.00025;  // Elemental time step
  const double rho_w = 9.825;     // Depth to pressure conversion for water on earth.
  const double rho_h = 0.25;      // Resistivity of horizontal pipes (not an accurate model).
  const double rho_v = 0.15;      // Resistivity of vertical pipes (a very inaccurate model!).

  const double pipe_length_l1 = 9.0;
  const double pipe_length_l2 = 2.0;
  const double pipe_length_l3 = 110.0;
  const double pipe_length_l4 = 3.5;
  const double pipe_length_l5 = 12.0;

  const double bucket_area1 = 33.0;
  const double bucket_area2 = 10.0;

  // State Variables - fill depth in each bucket
  double sv_D1, sv_D2;


  double time = 0.0;

  // External stimulus : input flow to bucket 1.
  public double stimulus()
  {
    const double f0_rate = 12.5;
    double r = 0.0;
    if (time > 1.0 && time < 2.0) r = f0_rate;
    if (time > 4.0 && time < 4.25) r = f0_rate;
    return r;
  }

  public int RunFDTD(uint sim_steps)
  {
    Console.WriteLine("Start WaterNetwork1 Simulation");
    plotter.Open(width, height);
    for (int it=0; it<sim_steps; it++)
      {
	time += deltaT;

	const double r1 = rho_h * pipe_length_l1 / rho_w;
	const double r2 = rho_v * pipe_length_l2 / rho_w;
	const double r3 = rho_v * pipe_length_l3 / rho_w;
	const double r4 = rho_v * pipe_length_l4 / rho_w;
	const double r5 = rho_h * pipe_length_l5 / rho_w;
        const double r35 = r3 + r5;

	// The easy flow equations:
	double f0 = stimulus();
	double f4 = sv_D2 / r4;

	// These ones are the solution of simultaneous equations done by hand on a sheet of paper.
	// (Or else use the resistors in parallel and potential divider formulae ...)
	// But the matrix flow form of this problem will automate this.
	double v1 = r35 * (sv_D1/r1 + sv_D2/r2) / (1 + r35/r1 + r35/r2);
	double f2 = (v1 - sv_D2) / r2;
	double f3 = v1 / r35; 
	double f1 = f2+f3;
	// The above details are unimportant - the point is they are labour-intensive to get right!	

	// Update state variables:
	sv_D1 += (f0-f1) / bucket_area1 * deltaT;
	sv_D2 += (f2-f4) / bucket_area2 * deltaT;

	// Check for bucket overflow
	if (sv_D1 > 1.0)
	  {
	    Console.WriteLine("Bucket 1 overflow\n"); break;
	  }
	if (sv_D2 > 1.0)
	  {
	    Console.WriteLine("Bucket 2 overflow\n"); break;
	  }

	// Console.WriteLine("time={0},  F1={1},  F2={2} F3={3} F4={4}", time, f1, f2, f3, f4);
	Console.WriteLine("time={0},  D1={1},  D2={2}", time, sv_D1, sv_D2);

	const double hscale = 30.0, vscale = (double)(height * 2);
	plotter.DrawPoint((uint)(time * hscale), height-30-(uint)(0.5 + sv_D1 * vscale), 2); // blue
 	plotter.DrawPoint((uint)(time * hscale), height-30-(uint)(0.5 + sv_D2 * vscale), 3); // red time domain plot
     }
     return 0;
  }
}


class TestBench
{
  public static int Main()
  {
    uint sim_steps = 6000 * 30; 
    WaterNetwork1 sim = new WaterNetwork1();
    sim.RunFDTD(sim_steps);
    return 0;
  }
}


// Sir George Stokes, who introduced Reynolds numbers, would probably have a fit if he saw the rather naive
// models of flow down pipes embodied above, but we don't care. His equations can be plugged in just as easily.


//   I1*R1 = I2*R2 = Vnode.  I1 = I2*R2/R1.  I1/I2 = R2/R1.
//eof

