//
// CholeskyDemo 2014: DJ Greaves. 
// (Based on http://rosettacode.org/wiki/Cholesky_decomposition).
// 
public class CholeskyDemo
{
    

    public static double[][] Cholesky(double[][] Adata)
    {
	int m = Adata.length;
	double[][] R = new double[m][m];//Default 0 for unassigned upper triangle.
	for(int i = 0; i<m ;i++)
	    for(int j = 0; j < i+1; j++)//Fill in diagonal and below only.
		{
		    double sum = 0.0;
		    for(int k = 0; k < j; k++) sum += R[i][k] * R[j][k];
		    R[i][j] = (i == j) ? Math.sqrt(Adata[j][j] - sum):
                                         (Adata[i][j] - sum)/R[j][j];
		}
	return R;
    }
    

    public static void test(double [][] Adata)
    {
	System.out.printf("Input data:\n");
	SimuSolve.printa(Adata);
	double[][] c = Cholesky(Adata);
	System.out.printf("Cholesky:\n");
	SimuSolve.printa(c);
	System.out.printf("Back subst:\n");
	SimuSolve.printa(SimuSolve.mpx(c, SimuSolve.transpose(c)));

    }

    public static void main(String[] args)
    {
	double[][] test1 = {{25, 15, -5},
			    {15, 18, 0},
			    {-5, 0, 11}};

	double[][] test2 = {{18, 22, 54, 42},
			    {22, 70, 86, 62},
			    {54, 86, 174, 134},
			    {42, 62, 134, 106}};

	test(test1);
	test(test2);
    }





}

// eof
