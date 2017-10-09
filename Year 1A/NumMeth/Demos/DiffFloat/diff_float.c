//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# 
//# Numerical differentiation.
//#
//# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.

#include <stdio.h>

/* Test numerical differentiation */

/* Use numbers like 1/3, 11/10, 8/7 which are recurring in binary so
** arithmetic problems are not hidden by values being exacly representable.
*/

static float h;

static float fun(float x) { return x*x + x/3 + 14.0/13 ; }
static float fundash(float x) { return 2*x + 1.0/3; }
static float d_fun_bydx(float x) { return (fun(x+h) - fun(x)) / h; }

int main()
{ 
  h = 8.0/7.0;
  while (h>1e-8)
  {  printf("h = %e,  d_fun_bydx(1.1) = %e, fun'(1.1) = %f\n",
            h, d_fun_bydx(1.1), fundash(1.1));
     h /= 2;
  }

  return 0;
}

// eof
