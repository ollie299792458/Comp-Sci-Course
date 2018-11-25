// University of Cambridge Computer Laboratory - Numerical Methods Demos.
// (C) 2014 DJ Greaves, University of Cambridge, Computer Laboratory.
// 
// TankExample2 - Time-varying simulation in circuit-flow (Spice) style.
// Tank or Tank example - both vary in stored quantity according to the integral of the in and out flows.
//
public class TankExample2 
{

    public static void main (String [] args)
    {
	double [] v = { 9.0, 0.6 }; // Starting values.
	
	double delta_t = 1e-5;  // Time step, 10 us (microsecond).
	double capacity = 1e-6; // 1uF (one microfarad). 

	double sim_time = 0.0;
	while (sim_time < 1.0e-2) // Simulate until end time.
	    {
		double V_c = v[1];
		double G_c = capacity / delta_t;
		double I_c = G_c * V_c; 

		double I_ctrue = (v[0]-v[1])/1000.0;
		System.out.printf("Capacitor (tank): time=%f   v_1=%f v_2=%f  \n", sim_time, v[0], v[1]);
		double [] [] G = {{0.011, -0.001}, {-0.001, 0.001+G_c}};
		double [] rhs = {0.1, I_c};

		// Note: This call to SimuSolve should really be multiple calls when non-linear components are also present.
		double [] v_primed = SimuSolve.Solve(G, rhs);

		// Forward differences
		v = v_primed;
		v[1] = v[1] + I_ctrue * delta_t / capacity;

		sim_time += delta_t; // Could use a FOR loop, but perhaps best not with doubles.
	    }

    }

}

// gnuplot runes:
// System.out.printf("%f   %f %f  \n", sim_time, v[0], v[1]);
//  set key right bottom
// plot [0:0.004]"cap-plot" using 1:2 with lines title "v1", "cap-plot" using 1:3 with lines title "v2"

// eof





