
public class CountingFloat {

	static void showval(float f) {
		int n = Float.floatToIntBits(f);
		System.out.printf("%.8e (0x%08x or %d:%06x)\n", 
				f, n, n>>23 & 0xff, n & 0x7fffff);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		float i, j;
		i = 0;
		do {
			j = i;
			i++;
		} while (i != j);
		showval(i);
	}

}




