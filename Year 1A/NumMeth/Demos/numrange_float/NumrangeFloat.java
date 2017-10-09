/**
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.

 * shows the range expressible as 'float' numbers, including dnorm/infinity 
 */

/**
 * @author silviabreu
 *
 */
public class NumrangeFloat {

	static void showval(float seed, int i, float f) {
		int n = Float.floatToIntBits(f);
		System.out.printf("%f * 2^%d = %e (0x%08x or %d:%06x)\n", seed, i, f, n,
		           n>>23 & 0xff, n & 0x7fffff);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  int i;
		  float seed = (float) 1.75;
		  float f, fold;
		  f = seed; i = 0;
		  do {  
			 showval(seed, i, f);
		     i++;
		     fold = f;
		     f *= 2.0;
		  } while (f != fold);
		  System.out.printf("========\n");
		  f = seed; i = 0;
		  do
		  {  showval(seed, i, f);
		     i--;
		     fold = f;
		     f /= 2.0;
		  } while (f != fold);
		  System.out.printf("========\n");
	}

}

