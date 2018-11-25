//
// LUdemo

// (C) 2014 DJ Greaves, University of Cambridge, Computer Laboratory.
//
// This is the 'unblocked' or naive implementation - using this will trash the cache for the large arrays that typically arise!
//

public class LUdemo
{

    // To see it fail, try test_fails;

    static double[][] test_fails = {{1, 2, 3, 1}, {4, 5, 6, 1}, {7, 8, 9, 1}, {1, 1, 1, 1}};

    static double[][] test_works = {{10, 2, 3, 1}, {4, 5, 6, 1}, {7, 8, 9, 1}, {1, 1, 1, 1}};

    static double[][] test_coefs = (true) ? test_works: test_fails;

    static double [] b = { 1, 1, 1, 1}; // An example rhs (actually same as bottom row of coefs so we know what the correct answer is by inspection!).

    public static void main (String [] args)
    {
	System.out.printf("L/U Decomposition\n");
	
	double [][] A = SimuSolve.copy2d(test_coefs); // Need to copy this since gets muted !!!!  
	System.out.printf("Initial A Matrix:\n"); SimuSolve.printa(A);
	System.out.printf("Example rhs b=:\n"); SimuSolve.printa(b);

	double [] sol = SimuSolve.SolveVerbose(A,b); 
	// The wrapping of the L/U decom inside Solve is just for demo purposes. In reality we would decompose 
	// once and then solve for multiple b.

	System.out.printf("Solution:\n"); SimuSolve.printa(sol);
	
	// Now see if it fits
	System.out.printf("Does it work y=:\n"); SimuSolve.printa(SimuSolve.mpx(test_coefs, sol));	
	System.out.printf("Finished.\n");

    }

}


// eof



