

// note - DJG 2015 - this makes use of code that is perhaps no longer supported in standard Java dists.

/* Even Java's otherwise well-specified libraries have bugs.  Try the   */
/* following code for X values of 1.0, 1.0e100, 1.0e200, 1.0e308.   All */
/* four should give similar results (perhaps differing by 1ulp due to   */
/* rounding of 10^n) but don't...                                       */
/* Java (at least javac 1.6.0_04) mis-implements complex division       */
/* for large numbers (think what the obvious maths definition is -- and */
/* and then note that someone has implemented this without thinking!).  */
/* Note the additional subtlety for X=1e308 in that the absolute value  */
/* of some of the complex numbers are not IEEE representable.           */

public class ComplexDiv {
	
	public static DoubleComplex f(DoubleComplex a, DoubleComplex b) {
		return DoubleComplex.divide(a, b);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double X = 1.0e308; /* 1.0 or or 1.0e100 or 1.0e200 or 1.0e308; */
		int n;
		for (n = 0; n<20; n++) {   
			DoubleComplex a = new DoubleComplex(X, X);
			DoubleComplex b = new DoubleComplex(X, (n/10.0)*X);
			DoubleComplex r = f(a, b);
			System.out.println(r);//printf("(%s)/(%s) => (%s)\n", a, b, r);
		}
	}

}

// eof

