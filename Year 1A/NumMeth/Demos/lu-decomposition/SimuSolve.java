//
//
//  Simulataneous Equations Solving using L/U decomposition.
//  (C) 2014-15 DJ Greaves.
//
// This copy in Java.

public class SimuSolve
{

    static double [] [] Lresult;

    public static double [] Solve(double [] [] G, double [] rhs)
    {
	//System.out.printf("L/U Decomposition Solver\n");
	double [][] A = copy2d(G);  // Need to copy if dont want to mutate input operand.
	LUdecompose(A, true);
	double [] y = FwdsSubst(Lresult, rhs); 
	double [] x = BackSubst(A, y);
	return x;
    }

    public static double [] SolveVerbose(double [] [] G, double [] rhs)
    {
	System.out.printf("L/U Decomposition Solver\n");
	
	double [][] A = copy2d(G);  // Need to copy if dont want to mutate.
	System.out.printf("SimuSolve Initial A Matrix:\n"); SimuSolve.printa(A);
	LUdecompose(A, true);
	System.out.printf("UU=\n"); SimuSolve.printa(A);
	System.out.printf("LL=\n"); SimuSolve.printa(Lresult);

	System.out.printf("Recombine LL and RR: Should result in the original:\n"); SimuSolve.printa(mpx(Lresult, A));

	double [] y = FwdsSubst(Lresult, rhs); 
	System.out.printf("After fwds subst=\n"); SimuSolve.printa(y);
	double [] x = BackSubst(A, y);
	return x;
    }



    public static void LUdecompose(double [][] Adata, boolean pivot_enable)
    {
	Lresult = new double [Adata.length][Adata.length];
	for (int k=0; k<Adata.length; k++) { Lresult[k][k] = 1.0; }
	
	for (int k=0; k<Adata.length; k++)
	    {
   	        double [] [] Ltemp = new double [Adata.length][Adata.length];
		for (int z=0; z<Adata.length; z++) { Ltemp[z][z] = 1.0; }
		if (pivot_enable)
		    {
			double p = 0.0;
			int k1 = 0;
			for(int i=k; i<Adata.length; i++)
			    {
				if (Math.abs(Adata[i][k]) > p) // Pivoting : find largest 
				    { p = Math.abs(Adata[i][k]);
					k1 = k;
				    }
			    }		
			//System.out.printf("Pivot %d %f\n", k1, p);
			double [] t = Adata[k]; Adata[k] = Adata[k1]; Adata[k1] = t;
		    }
		for (int i=k+1; i<Adata.length; i++)
		    {
			assert(Adata[k][k] != 0.0); // Singular matrix!
			double mu = Adata[i][k]/Adata[k][k];
			Adata[i][k] = 0.0;
			Lresult [i][k] = mu; // This simple store is all you need
			for (int j=k+1; j<Adata[i].length; j++)
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
	double [] y = new double[L.length];
	y[0] = b[0]; 
	for (int i=1; i < L.length; i++)
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
	double [] x = new double[UU.length];
	for (int i=UU.length-1; i >= 0; i--)
	    {
		double sum = 0.0;
		for (int j=i+1; j < UU.length; j++) sum += UU[i][j] * x[j];
		x[i] = (y[i] - sum)/UU[i][i];
	    }
	return x;
    }


    //------------------------------------------------------------------------
    // Matrix support functions now follow (there is similar code in java.utils).

    public static void printa (double [][] A)
    {
	for (int i=0; i<A.length; i++)
	    {
		System.out.printf("    ");
		for (int j=0; j<A[i].length; j++)
		    {
			if (A[i][j]==0.0) System.out.printf("-.----- ", A[i][j]);
			else System.out.printf("%1.5f ", A[i][j]);
		    }
		System.out.printf("\n");
	    }
	System.out.printf("\n");
    }

    public static void printa (double []A)
    {
	System.out.printf("{");
	for (int j=0; j<A.length; j++)
	    {
		if (A[j]==0.0) System.out.printf("-.----- ", A[j]);
		else System.out.printf("%1.5f ", A[j]);
	    }
	System.out.printf("}\n");
    }


    public static double[][] mpx(double[][] AA, double[] BB)
    {
	double [][] BP = new double[BB.length][1]; // Convert to proper column vector (do a transpose and type change).
	for (int i=0; i<BB.length; i++) BP[i][0] = BB[i];
	return mpx (AA, BP);
    }

    public static double[][] mpx(double[][] AA, double[][] BB)
    {
	//System.out.printf(" mpx %d,%d with %d,%d\n", AA.length, AA[0].length, BB.length, BB[0].length);	
	assert(AA[0].length == BB.length);
	double[][] Ans = new double[AA.length][BB[0].length];
	for (int i=0; i<AA.length; i++)
	    for (int k=0; k<BB[0].length; k++)
		{
		    double sum = 0.0;
		    for (int j=0; j<AA[0].length; j++) 
			{
			    //System.out.printf(" mpx %d,%d with %d,%d %d %d %d\n", AA.length, AA[0].length, BB.length, BB[0].length, i, k, j);	
			    sum += AA[i][j] * BB[j][k];
			}
		    Ans[i][k] = sum;
		}
	return Ans;
    }

    public static double[][] transpose(double[][] AA)
    {
	double[][] Ans = new double[AA[0].length][AA.length];
	for (int i=0; i<AA.length; i++)
	    for (int j=0; j<AA[0].length; j++) Ans[j][i] = AA[i][j];
	return Ans;
    }

    public static double [] [] copy2d(double [] [] matrix) // A deep clone.
     {
	double [][] copy = new  double[matrix.length][];
	for(int i = 0; i < matrix.length; i++) copy[i] = matrix[i].clone();
	return copy;
    }
}

// eof
