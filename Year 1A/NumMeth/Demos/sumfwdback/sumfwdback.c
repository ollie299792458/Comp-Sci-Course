//#
//# Adding up forwards and backwards
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.
//#


#include <math.h>
#include <stdio.h>

/* Do a friends of one of FHK's problems.  Sum(n=0..infinity) 1/(n+pi). */
/* First forwards (finding out where no futher change happens, and then */
/* backwards.  Note the maths says the answer is infinite...            */

double g(int i) { return 1.0/(i + M_PI); }

int main()
{   double d, dd;
    int n = 0, i;
    float f=0, ff;
    do { ff = f;
         f += 1.0/(n + M_PI);
         n++;
    } while (f != ff);
    printf("Forward (float) sum until no change: %11.7f at n=%d\n", f, n);
    f = 0;
    for (i = n-1; i>=0; i--)
        f += 1.0/(i + M_PI);
    printf("Backward (float) sum:                %11.7f\n", f);
    d = 0;
    for (i = 0; i<n; i++)
        d += g(i);
    printf("Forward (double) sum:                %20.16f\n", d);
    d = 0;
    for (i = n-1; i>=0; i--)
        d += g(i);
    printf("Backward (double) sum:               %20.16f\n", d);
/* now use Kahan float summation, using ff as guard digits...           */
    ff = 0; f = g(0);
    for (i = 1; i<n; i++)
    {   float y  = g(i) - ff; 
        float t = f + y;
        ff = (t - f) - y;
        f = t;
    }
    printf("Forward (float) Kahan sum:           %11.7f\n", f);
    ff = 0; f = g(n-1);
    for (i = n-2; i>=0; i--)
    {   float y  = g(i) - ff; 
        float t = f + y;
        ff = (t - f) - y;
        f = t;
    }
    printf("Backward (float) Kahan sum:          %11.7f\n", f);
/* now use Kahan summation, using dd as guard digits...                 */
    dd = 0; d = g(0);
    for (i = 1; i<n; i++)
    {   double y  = g(i) - dd; 
        double t = d + y;
        dd = (t - d) - y;
        d = t;
    }
    printf("Forward (double) Kahan sum:          %20.16f\n", d);
    dd = 0; d = g(n-1);
    for (i = n-2; i>=0; i--)
    {   double y  = g(i) - dd; 
        double t = d + y;
        dd = (t - d) - y;
        d = t;
    }
    printf("Backward (double) Kahan sum:         %20.16f\n", d);

    return 0;
}


// eof
