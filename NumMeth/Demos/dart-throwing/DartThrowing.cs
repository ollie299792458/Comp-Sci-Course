//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
//
// Monte-Carlol Simulation to find Pi - Dart Throwing
//
using System;
using System.IO;
using System.Text;

class DartThrowing
{
  public static int N = 1000000;
  public double StatsRun(int N0, int r0, bool anti, out double error)
  {
    N = N0;
    const int runs = 50;
    double m0 = 0.0, m1=0.0, m2=0.0;
    for (int run = 0; run < runs; run++)
      {
	double x = OneRun("StatsRun", r0 + run, anti);
	m0 += 1;
	m1 += x;
	m2 += x*x;
      }
    double mean = m1/m0;
    double pi = Math.PI;
    error = Math.Abs(mean - pi)/pi;
    double variance =  m2/m0 - mean*mean;
    Console.WriteLine("Mean of {0} is {1} with variance {2} and error {3}", m0, mean, variance, error);
    return variance;
  }

  public double OneRun(string title, int seed, bool antithetic)
  {
    var rg = new Random(seed);
    int v = 10000;
    int v2 = v * v;

    int j0 = rg.Next(v);
    bool toggle = false;	
    int insides = 0;
    int j1=0, j2=0, j3=0;
    for (int n=0;n<N; n++)
      {
	//Console.WriteLine(k);
	j3=j2; j2 = j1; j1 = j0;
	if (toggle) j0 = v-j3; else j0 = rg.Next(v);
	if (j0*j0 + j1*j1 < v2) insides += 1;
	if (antithetic) toggle = !toggle;
      }
    double ans =  4.0 * (float)insides/(float)N;
    Console.WriteLine("{3}: Antithetic={4} Ratio is {0}/{1} = {2}", insides, N, ans, title, antithetic);
    return ans;
  }
}

class TestBench
{

  public static int Main()
  {
    DartThrowing sim = new DartThrowing();
    sim.OneRun("run0",  0, false);
    sim.OneRun("run1",  1, false);
    sim.OneRun("run3",  3, false);
    return 0;
  }
}


//    System.Threading.Thread.Sleep(1000);
//    Console.ReadKey();

// eof
