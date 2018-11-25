//
// LUdemo

// (C) 2014 DJ Greaves, University of Cambridge, Computer Laboratory.
//
// This is the 'unblocked' or naive implementation - using this will trash the cache for the large arrays that typically arise!


//  using KiwiSystem;

using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;




public class d2A
{
  public static double [] [] unpack(double [,] arg)
  {
    double [] [] ans = new double [arg.GetLength(0)] [];

    for (int jj=0; jj < arg.GetLength(0); jj++)
      {
	
	double [] row = new double [arg.GetLength(1)];
	for (int kk=0; kk < arg.GetLength(1); kk++) row[kk] = arg[jj,kk];
	ans[jj] = row;
      }
    return ans;
  }

}
public class LUdemo
{

    // To see it fail, try test_fails;



 public static class testset
  {
    
    public static double [,] test_fails = new double [,] {{1, 2, 3, 1}, {4, 5, 6, 1}, {7, 8, 9, 1}, {1, 1, 1, 1}};
    
    public static double [,] test_works = new double [,] {{10, 2, 3, 1}, {4, 5, 6, 1}, {7, 8, 9, 1}, {1, 1, 1, 1}};
    
    public static double [][] test_coefs = d2A.unpack((true) ? test_works: test_fails);

    public static double [] b = { 1, 1, 1, 1}; // An example rhs (actually same as bottom row of coefs so we know what the correct answer is by inspection!).
       
 }

    public static void Main (String [] args)
    {
	Console.WriteLine("L/U Decomposition\n");
	
	double [][] A = (double [][])testset.test_coefs.Clone(); // Need to copy this since gets muted !!!!  
	Console.WriteLine("Initial A Matrix:\n"); SimuSolve.printa(A);
	Console.WriteLine("Example rhs b=:\n"); SimuSolve.printa(testset.b);

	double [] sol = SimuSolve.SolveVerbose(A, testset.b); 
	// The wrapping of the L/U decom inside Solve is just for demo purposes. In reality we would decompose 
	// once and then solve for multiple b.

	Console.WriteLine("Solution:\n"); SimuSolve.printa(sol);
	
	// Now see if it fits
	Console.WriteLine("Does it work y=:\n"); SimuSolve.printa(SimuSolve.mpx(testset.test_coefs, sol));	
	Console.WriteLine("Finished.\n");

    }

}


// eof



