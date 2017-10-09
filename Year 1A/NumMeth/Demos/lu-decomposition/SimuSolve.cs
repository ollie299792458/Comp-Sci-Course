//
//
//  Simulataneous Equations Solving using L/U decomposition.
//  (C) 2014-15 DJ Greaves.
//
//  This copy in C#.

using System;
using System.Diagnostics;

public class SimuSolve
{

    static double [] [] Lresult;

    public static double [] Solve(double [] [] G, double [] rhs)
    {
	//Console.WriteLine("L/U Decomposition Solver\n");
	double [][] A = copy2d(G);  // Need to copy if dont want to mutate input operand.
	LUdecompose(A, true);
	double [] y = FwdsSubst(Lresult, rhs); 
	double [] x = BackSubst(A, y);
	return x;
    }

    public static double [] SolveVerbose(double [] [] G, double [] rhs)
    {
	Console.WriteLine("L/U Decomposition Solver\n");
	
	double [][] A = copy2d(G);  // Need to copy if dont want to mutate.
	Console.WriteLine("SimuSolve Initial A Matrix:\n"); SimuSolve.printa(A);
	LUdecompose(A, true);
	Console.WriteLine("UU="); SimuSolve.printa(A);
	Console.WriteLine("LL="); SimuSolve.printa(Lresult);

	Console.WriteLine("Recombine LL and RR: Should result in the original:"); SimuSolve.printa(mpx(Lresult, A));

	double [] y = FwdsSubst(Lresult, rhs); 
	Console.WriteLine("After fwds subst="); SimuSolve.printa(y);
	double [] x = BackSubst(A, y);
	return x;
    }



    public static void LUdecompose(double [][] Adata, bool pivot_enable)
    {
	Lresult = new double [Adata.Length] [];
        for (int k=0; k<Adata.Length; k++) Lresult[k] = new double [Adata[k].Length];
	for (int k=0; k<Adata.Length; k++) { Lresult[k][k] = 1.0; }
	
	for (int k=0; k<Adata.Length; k++)
	    {
   	        double [] [] Ltemp = new double [Adata.Length] [];
                for (int k1=0; k1<Adata.Length; k1++) Ltemp[k1] = new double [Adata[k1].Length];
		for (int z=0; z<Adata.Length; z++) { Ltemp[z][z] = 1.0; }
		if (pivot_enable)
		    {
			double p = 0.0;
			int k1 = 0;
			for(int i=k; i<Adata.Length; i++)
			    {
				if (Math.Abs(Adata[i][k]) > p) // Pivoting : find largest 
				    { p = Math.Abs(Adata[i][k]);
				      k1 = k;
				    }
			    }		
			//Console.WriteLine("Pivot %d %f", k1, p);
			double [] t = Adata[k]; Adata[k] = Adata[k1]; Adata[k1] = t;
		    }
		for (int i=k+1; i<Adata.Length; i++)
		    {
			Debug.Assert(Adata[k][k] != 0.0); // Singular matrix!
			double mu = Adata[i][k]/Adata[k][k];
			Adata[i][k] = 0.0;
			Lresult [i][k] = mu; // This simple store is all you need
			for (int j=k+1; j<Adata[i].Length; j++)
			    {
				Adata[i][j] = Adata[i][j] - mu *Adata[k][j];
				Ltemp[i][k] = mu; 
			    }
		    }


		//SimuSolve.printa(Adata);
		//Wasteful way to do it:
                // Lresult = mpx(Ltemp, Lresult);


	    }
    }

    public static double [] FwdsSubst(double [][] L, double [] b)
    // Forwards substitution to solve Ly=b
    {
	double [] y = new double[L.Length];
	y[0] = b[0]; 
	for (int i=1; i < L.Length; i++)
	    {
		double sum = 0.0;
		for (int j=0; j < i; j++) sum += L[i][j] *  y[j];
		y[i] = b[i] - sum;
	    }
	return y;
    }

    public static double [] BackSubst(double [][] UU, double [] y)
    // Back substitution to solve Ux=y
    {
	double [] x = new double[UU.Length];
	for (int i=UU.Length-1; i >= 0; i--)
	    {
		double sum = 0.0;
		for (int j=i+1; j < UU.Length; j++) sum += UU[i][j] * x[j];
		x[i] = (y[i] - sum)/UU[i][i];
	    }
	return x;
    }


    //------------------------------------------------------------------------
    // Matrix support functions now follow (there is similar code in java.utils).

    public static void printa (double [][] A)
    {
	for (int i=0; i<A.Length; i++)
	    {
		Console.Write("    ");
		for (int j=0; j<A[i].Length; j++)
		    {
			if (A[i][j]==0.0) Console.Write("-.----- ", A[i][j]);
			else Console.Write("{0:0.000} ", A[i][j]);
		    }
		Console.WriteLine("");
	    }
	Console.WriteLine("");
    }

    public static void printa (double []A)
    {
	Console.Write("{");
	for (int j=0; j<A.Length; j++)
	    {
		if (A[j]==0.0) Console.Write("-.----- ", A[j]);
		else Console.Write("{0:0.0000} ", A[j]);
	    }
	Console.WriteLine("}");
    }


    public static double[][] mpx(double[][] AA, double[] BB)
    {
	double [][] BP = new double[BB.Length][]; // Convert to proper column vector (do a transpose and type change).
	for (int i=0; i<BB.Length; i++) 
	    {
		BP[i] = new double[1];
		BP[i][0] = BB[i];
	    }
	return mpx (AA, BP);
    }

    public static double[][] mpx(double[][] AA, double[][] BB)
    {
	//Console.WriteLine(" mpx %d,%d with %d,%d\n", AA.Length, AA[0].Length, BB.Length, BB[0].Length);	
	Debug.Assert(AA[0].Length == BB.Length);
	double[][] Ans = new double[AA.Length][];
	for (int k1=0; k1<AA.Length; k1++) Ans[k1] = new double [AA[k1].Length];	
	for (int i=0; i<AA.Length; i++)
	    for (int k=0; k<BB[0].Length; k++)
		{
		    double sum = 0.0;
		    for (int j=0; j<AA[0].Length; j++) 
			{
			    //Console.WriteLine(" mpx %d,%d with %d,%d %d %d %d\n", AA.Length, AA[0].Length, BB.Length, BB[0].Length, i, k, j);	
			    sum += AA[i][j] * BB[j][k];
			}
		    Ans[i][k] = sum;
		}
	return Ans;
    }

    public static double[][] transpose(double[][] AA)
    {
	double[][] Ans = new double[AA[0].Length][];
	for (int k1=0; k1<AA.Length; k1++) Ans[k1] = new double [AA[k1].Length];
	for (int i=0; i<AA.Length; i++)
	    for (int j=0; j<AA[0].Length; j++) Ans[j][i] = AA[i][j];
	return Ans;
    }

    public static double [] [] copy2d(double [] [] matrix) // A deep clone.
     {
	double [][] copy = new  double[matrix.Length][];
	for(int i = 0; i < matrix.Length; i++)
	    {
		copy[i] = new double [matrix[i].Length];
		for (int k1=0; k1<matrix[i].Length; k1++) copy[i][k1] = matrix[i][k1];
	    }
	return copy;
    }
}

// eof
