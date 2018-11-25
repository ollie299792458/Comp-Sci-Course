//
// Long Multiplication Techniques...
//
public class TwoMultiplies 
{
    final static int base = 10;
    static int [] xd, yd, rd;

    static void naive_method()
    {
	for (int x=0; x<xd.length; x++) 
	    for (int y=0; y<yd.length; y++) 
		{
		    int b = x+y;
		    int s = xd[x] * yd[y];
		    //if (s> 0) System.out.println("x=" + x + " y=" + y + " s=" + s);
		    while (s > 0)
			{
			    s += rd[b];
			    rd[b++] = s % base;
			    s = s / base;
			}
		}
	// Do we need to check most-significant result bit is less than base?
	}


    static void vehdic_method()
    {
	int b, c_in = 0;
	for (b=0; b<xd.length+yd.length; b++) 
	    {
		int s = c_in;
		for (int x=0; x<=b; x++)
		    {
			int y = b-x;
			if (x >= xd.length || y >= yd.length) continue;
			s += xd[x] * yd[y];
		    }
		rd[b] = s % base;
		c_in = s / base;
	    }
    }

    public static void main(String[] args) 
    {

	for (int test=0; test<2; test++)
	    {
		xd = new int [10];
		yd = new int [10];
		rd = new int [20];
		for (int i=0; i<xd.length; i++) xd[i] = base-1;
		for (int i=0; i<xd.length; i++) yd[i] = base-1;

		if (test==1) vehdic_method(); else naive_method();

		
		for (int i=rd.length-1;i>=0; i--) System.out.print(" " + rd[i]);
		System.out.println();
	    }
    }
    
}

/*
Output
javac TwoMultiplies.java
java -cp . TwoMultiplies
 9 9 9 9 9 9 9 9 9 8 0 0 0 0 0 0 0 0 0 1
 9 9 9 9 9 9 9 9 9 8 0 0 0 0 0 0 0 0 0 1

*/