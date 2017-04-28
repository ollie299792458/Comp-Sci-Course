//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# 
//# Numerical differentiation.
//#
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.


public class DiffFloat {

	static float h;

	static float fun(float x) { 
		return (float) (x*x + x/3 + 14.0/13) ; 
	}
	
	static float fundash(float x) { 
		return (float) (2*x + 1.0/3); 
	}
	
	static float d_fun_bydx(float x) { 
		return (fun(x + h) - fun(x)) / h; 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		h = (float) (8.0/7.0);
		while (h > 1e-8) {  
			System.out.printf("h = %e,  d_fun_bydx(1.1) = %e, fun'(1.1) = %f\n",
					h, d_fun_bydx((float) 1.1), fundash((float) 1.1));
			h /= 2;
		}	
	}
}



// eof
