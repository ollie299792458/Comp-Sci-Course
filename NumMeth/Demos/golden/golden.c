//#
//# Golden Ratio Iteration
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.
//#

#include <stdio.h>
#include <math.h>



int main()
{   double g = (sqrt(5.0) + 1.0)/2.0;
    double x = 2.0;
    double preverr = 1.0;
    int i;
    for (i = 1; i<35; i++)
    {   x = sqrt(x+1.0);
        printf("i = %.2d, x = %18.16f, err = %10.4e, err/prev.err = %10.4e\n",
               i, x, x-g, (x-g)/preverr);
        preverr = x-g;
    }
    return 0;
}

//eof
