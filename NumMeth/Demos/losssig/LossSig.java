//#  Loss of significance demo.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.


/**
 * @author silviabreu
 *
 */
public class LossSig {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int i;
	    double x = 1.0/9.0;
	    x *= 10.0;
	    for (i = 0; i < 30; i++) {   
	    	System.out.printf("%e\n", x);
	        x = (x - 1.0) * 10.0;  
	    }
	}
}


