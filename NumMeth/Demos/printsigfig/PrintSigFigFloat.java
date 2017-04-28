
//# Investigation of printing significant figures.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu

public class PrintSigFigFloat {

	static void showval(float f) {
		int n = Float.floatToIntBits(f);
		System.out.printf("%.7e %.8e (0x%8x or %d:%6x)\n", f, f, n,
		           n>>23 & 0xff, n & 0x7fffff);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  
		float f = 99;
		int n = Float.floatToIntBits(f);
		showval(f);     // show 99.0
		n++;            // 99.0 with lsbit incremented, so 99.0000x
		f = Float.intBitsToFloat(n);
		showval(f);     // show it, noting how it affects the decimal.
		f = 127;        // Repeat for 127.0 and its neighbours
		n = Float.floatToIntBits(f);
		showval(f);
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
		n++;            
		f = Float.intBitsToFloat(n);
		showval(f);     
	}
}

