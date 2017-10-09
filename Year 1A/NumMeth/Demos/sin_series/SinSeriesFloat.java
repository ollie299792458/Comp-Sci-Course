//#
//#  Taylor Series for sine - series summing
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.
//#



/**
 * @author silviabreu
 *
 */
public class SinSeriesFloat {

	static int iterations;
	static float max_term;

	static float mysin(float x)
	{   float s = 0, ss;   // sum
	    float t = x;       // term
	    int i = 1;         // count
	    do
	    {   ss = s;
	        s += t;
	        System.out.printf("  s = %f\n", s);
	        if (Math.abs(t) > Math.abs(max_term)) max_term = t;
	        t = - t*x*x/(i+1)/(i+2);
	        i += 2;
	        iterations++;
	    } while (s != ss);
	    return s;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int k;
		for (k = 0; k<=240; k++) {
			float x = (float) (k/10.0);
			float s;
			iterations = 0;
			max_term = 0;
			s = mysin(x);
			System.out.printf("mysin(%.1f) = %f [%d iterations, error=%.2e]\n",
					x, s, iterations, s - Math.sin(x));
			System.out.printf("                    max_term=%f, errnorm = %.2e\n",
					max_term, (s - Math.sin(x)) / max_term);
		}	
	}
	
}	

// eof
