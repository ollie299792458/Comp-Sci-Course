//#
//# Investigation of NaN.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu
//#



#include <stdio.h>
#include <math.h>

static void divzero_test(double x, double y) { printf("%e\n", x/y); }

static void sqrt_negative_test(double x) { printf("%e\n", sqrt(x)); }

int main() 
{ 
  divzero_test(0.0, 0.0); 

  sqrt_negative_test(-1); 

  return 0;
}


// eof
