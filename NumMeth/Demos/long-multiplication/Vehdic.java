//# Long Multiplication - Vhedic method in Java
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.
//#


public class Vehdic
{
 
    public int [] xd = new int [5];
    public int [] yd = new int [5];
    public int [] rd = new int [10];
    final int base = 32768;

    void vehdic_method()
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

    public void printit()
    {
	for (int i =0; i<rd.length; i++)
	    System.out.printf("     %d   %d \n", i, rd[i]);
    }

    
    public static void main(String [] args)
    {
	
	Vehdic mpx = new Vehdic();

	mpx.xd[0] = 1;
	mpx.xd[1] = 2;
	mpx.xd[2] = 3;

	mpx.yd[2] = 1;
	mpx.yd[3] = 1;

	mpx.vehdic_method();
	mpx.printit();
    }
    
}

// eof

