#include <complex.h>
#include <stdio.h>

/* The following program produces demonstrates a bug in gcc 4.1.2       */
/* (due to Nick Maclaren) when generating code for x86 architectures    */
/* running in 64-bit mode (these naturally use a different set of       */
/* floating point operations -- finally escaping from the 387 model).   */
/* The problem is that complex a/b naturally uses abs(b) and this can   */
/* (and does) overflow if coded naively (which it is).  Ho ho!          */
/* The compiler generates working code for x86 architectures running    */
/* in 32-bit mode.                                                      */
/* Try it on your own machine -- there's a bug if you see NaNs or infs. */

double complex f(double complex a, double complex b) { return a/b;}

int main()
{   double complex X = 1.0e308;
    int n;
    for (n = 0; n<20; n++)
    {   double complex a = X + X*I, b = X + (n/10.0)*X*I;
        double complex r = f(a,b);
        printf("(%.3e,%.3e)/(%.3e,%.3e) => (%.3e,%.3e)\n",a,b,r);
    }
    return 0;
}
