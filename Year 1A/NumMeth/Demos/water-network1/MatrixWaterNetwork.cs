//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.


// Water Network 1 - matrix form.


using System;
using System.IO;
using System.Text;
using System.Diagnostics;
using FlowNetworkStencils;


class MatrixWaterNetwork1
{

  const int width = 800, height = 600;
  bool plotting = true;
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
    Console.WriteLine("Start WaterNetwork1 Simulation - Flow Matrix Form");
    plotter.Open(width, height);

    const int nodes = 4;

    double [] [] GG  = new double [nodes] [];
    for (int k1=0; k1<nodes; k1++) GG[k1] = new double [nodes];
    double [] rhs = new double [nodes];
    FNS.fancy fancies = null;

	const double r1 = rho_h * pipe_length_l1 / rho_w;
	const double r2 = rho_v * pipe_length_l2 / rho_w;
	const double r3 = rho_v * pipe_length_l3 / rho_w;
	const double r4 = rho_v * pipe_length_l4 / rho_w;
	const double r5 = rho_h * pipe_length_l5 / rho_w;

    double [] v = new double [nodes];
    double old_stim = 0.0 ;

    FNS.add_resistor(GG, 0, 1, r1);
    FNS.add_resistor(GG, 1, 2, r2);
    FNS.add_resistor(GG, 1, 3, r3);
    FNS.add_resistor(GG, 2, -1, r4);
    FNS.add_resistor(GG, 3, -1, r5);
    fancies = new FNS.bucket(fancies, GG, rhs, v, 0, -1, "bucket1", bucket_area1);
    fancies = new FNS.bucket(fancies, GG, rhs, v, 2, -1, "bucket2", bucket_area2);

    for (int it=0; it<sim_steps; it++)
      {
	time += deltaT;

	rhs[0] -= old_stim;
        old_stim = stimulus();
	rhs[0] += old_stim;

	for (int wcount=0;wcount<1000; wcount++) // Iterate within the current time step for non-linear components.
	  {
	    bool converged = fancies.steady_state_iterate(deltaT);
	    double [] v_primed = SimuSolve.Solve(GG, rhs);
	    for(int k=0; k<nodes;k++) v[k] = v_primed[k];// Ideally Solve could be asked to do it in place
	    if (converged) break;  // Typically we will break out within a few iterations if time step is not too big.
	  }

	fancies.time_step_commit(deltaT);

	double sv_D1 = v[0];
	double sv_D2 = v[2];

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

	const double hscale = 30.0, vscale = (double)(height * 2.0);
	plotter.DrawPoint((uint)(time * hscale), height-30-(uint)(0.5 + sv_D1 * vscale), 2); // blue
 	plotter.DrawPoint((uint)(time * hscale), height-30-(uint)(0.5 + sv_D2 * vscale), 3); // red time domain plot
 	plotter.DrawPoint((uint)(time * hscale), height-30-(uint)(0.5 + v[1] * vscale), 4); // red time domain plot
     }
     return 0;
  }
}


class TestBench
{
  public static int Main()
  {
    uint sim_steps = 6000 * 30; 
    MatrixWaterNetwork1 sim = new MatrixWaterNetwork1();
    sim.RunFDTD(sim_steps);
    return 0;
  }
}


// Sir George Stokes, who introduced Reynolds numbers, would probably have a fit if he saw the rather naive
// models of flow down pipes embodied above, but we don't care. His equations can be plugged in just as easily.


//   I1*R1 = I2*R2 = Vnode.  I1 = I2*R2/R1.  I1/I2 = R2/R1.
//eof

