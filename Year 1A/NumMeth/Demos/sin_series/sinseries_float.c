//#
//#  Taylor Series for sine.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.
//#


#include <stdio.h>
#include <math.h>

/* Series summing */

static int iterations;
static float max_term;

static float mysin(float x)
{   float s = 0, ss;   // sum
    float t = x;       // term
    int i = 1;         // count
    do
    {   ss = s;
        s += t;
        printf("  s = %f\n", s);
        if (fabs(t) > fabs(max_term)) max_term = t;
        t = - t*x*x/(i+1)/(i+2);
        i += 2;
        iterations++;
    } while (s != ss);
    return s;
}

int main()
{   int k;
    for (k = 0; k<=240; k++)
    {   float x = k/10.0, s;
        iterations = 0;
        max_term = 0;
        s = mysin(x);
        printf("mysin(%.1f) = %f [%d iterations, error=%.2e]\n",
                x, s, iterations, s-sin(x));
        printf("                    max_term=%f, errnorm = %.2e\n",
                max_term, (s-sin(x))/max_term);
    }
    return 0;
}

// eof
