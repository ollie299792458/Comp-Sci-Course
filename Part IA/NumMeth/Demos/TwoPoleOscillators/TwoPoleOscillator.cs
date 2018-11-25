//
// FDTD Simulation of a Two-Pole Oscillator
//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
//
using System;
using System.IO;
using System.Text;


class TwoPoleOscillator // Quadrature Oscillator
{

  double x_state, y_state; // State Variables
 
  // Control Parameters
  double DeltaT = 0.009;

  public int RunFDTD_1(uint sim_steps)
  {
    Console.WriteLine("Start Oscillator Simulation");

    const int width = 600; //pixels
    const int height = width * 3 / 4;
    bool plotting = true;

    PlotUtil.WavFile wf = new PlotUtil.WavFile(sim_steps, 16);
    wf.write_hdr("2pole.wav");

    PlotUtil.Plotter plotter = new PlotUtil.Plotter();
    plotter.Open(width, height);

    // Initial conditions
    x_state = 1.0; y_state = 1.0;
    for (int it=0; it<sim_steps; it++)
    {
      x_state = x_state + DeltaT * y_state;
      y_state = y_state - DeltaT * x_state;
      x_state = x_state * 0.999; // Add in some resitive loss

      wf.write_val((int)(x_state * 32760));

      if (plotting)
      { int x = it / 6, gain = height/10;
        plotter.DrawPoint((uint)(x), (uint)(x_state*gain + 2*height/5), 2);
        plotter.DrawPoint((uint)(x), (uint)(y_state*gain + 3*height/5), 3);
      }
    Console.WriteLine(" {0} {1}", x_state, y_state);
    }
    wf.Close();
    return 0;
  }


  public int RunFDTD_2(uint sim_steps)
  {
    Console.WriteLine("Start Oscillator Simulation - Differentiating");

    const int width = 600; // pixels
    const int height = width * 3 / 4;
    bool plotting = true;

    PlotUtil.WavFile wf = new PlotUtil.WavFile(sim_steps, 8);
    wf.write_hdr("2pole-dif.wav");

    PlotUtil.Plotter plotter = new PlotUtil.Plotter();
    plotter.Open(width, height);

    // Initial conditions
    x_state = 100.0; y_state = 100.0;

    double x_old = 99.0, y_old = 99;
    for (int it=0; it<sim_steps; it++)
    {
      double k = 1/0.05;

      const bool with_zero = true;
      double x_dot, y_dot;

      double CR = 1.0;
      if (with_zero)
      {
      
        x_dot = x_state - x_old;
        y_dot = y_state - y_old;

        x_old += x_dot * DeltaT / CR; // old is used as voltage integrator for differentiator series capacitor.
        y_old += y_dot * DeltaT / CR;

        x_dot = x_state - x_old;
        y_dot = y_state - y_old;

      }
      else
      {
      
        x_dot = (x_state - x_old) / DeltaT;
        y_dot = (y_state - y_old) / DeltaT;
        x_old = x_state;
        y_old = y_state;
      }

      Console.WriteLine(" x={0} dx/dt={1}     y={2} dy/dt={3}", x_state, x_dot, y_state, y_dot);


      y_state = x_dot / -k; 
      x_state = y_dot;

      wf.write_val((int)(x_state * 1000)); // audio gain


      if (plotting)
      { int x = it / 10; // 
        const double vgain = height/0.1;
        // Plot x and y vars with origins 2/5 and 3/5 of ordinate range.
        plotter.DrawPoint((uint)x, (uint)(x_state*vgain + 2*height/5), 2);
	plotter.DrawPoint((uint)x, (uint)(y_state*vgain + 3*height/5), 3);
      }

    }
    wf.Close();
    return 0;
  }

}

class TestBench
{

  public static int Main()
  {
    uint sim_steps = 2000 * 20;
    TwoPoleOscillator sim = new TwoPoleOscillator();
    sim.RunFDTD_1(sim_steps);
    return 0;
  }
}

//eof

