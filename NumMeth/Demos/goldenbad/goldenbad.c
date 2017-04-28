//#
//#  Golden Ratio - gone bad.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.
//#

#include <stdio.h>
#include <math.h>


/* Iteration for golden ratio...                                        */

int main()
{   double g = (sqrt(5.0) + 1.0)/2.0;
    double x = g + 2.2e-16;    /* start one ULP out */
    double preverr = 1.0;
    int i;
    for (i = 1; i<35; i++)
    {   x = x*x - 1;
        printf("i = %.2d, x = %18.16f, err = %10.4e, err/prev.err = %10.4e\n",
               i, x, x-g, (x-g)/preverr);
        preverr = x-g;
    }
    return 0;
}

// eof
