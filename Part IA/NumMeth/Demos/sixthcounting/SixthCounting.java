/**
 * Show the dangers of counting with inexact floating point values --
 * (accidentally) double gets it `wrong' and float gets it `right'.
 */

/**
 * @author silviabreu
 *
 */
public class SixthCounting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final double sixth = 1.0/6.0;
		final float fsixth = 1.0f/6.0f;
		Double d; // Double rather than double to make sure it comes from memory not the register
		Float f; // likewise for the float value
		for (f = new Float(0.0); f < 1.0; f += fsixth) {
			System.out.printf("Here with float %.7f\n", f);
		}
		for (d = 0.0; d < 1.0; d += sixth) {
			System.out.printf("Here with double %.16f\n", d);
		}
	}

}
