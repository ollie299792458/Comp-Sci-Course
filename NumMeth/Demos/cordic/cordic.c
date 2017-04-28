//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (* An implementation and test of the CORDIC technique for sine and cosine *)
//# (* (C) 2015 DJ Greaves, University of Cambridge.                          *)

#include <stdio.h>
#include <math.h>


// For a fixed number of iterations we do not need a factor correction table, we can just use a single pre-scaled constant.

// Lookup table of angles generated once and for all with:  
//    cordic_ctab[i] = (int)(0.5 + atan((2 ^ -i) * 1e9));

int cordic_ctab[32] =
  {
    785398163,463647609,244978663,124354995,62418810,
    31239833,15623729,7812341,3906230,
    1953123,976562,488281,244141,122070,61035,
    30518,15259,7629,3815,1907,954,477,238,119,
    60,30,15,7,4,2,1,0 };


// See:
// http://www.dcs.gla.ac.uk/~jhw/cordic/
//
// The following low-level coding in C shows the lack of any multiplication - there are no asterisks. 
//
// The xor and subtract d is a way of conditional negation without control flow (if statements).
//
int cordic(int theta)
{ 
  int k, x=607252935 /*prescaled constant*/, y=0;   // w.r.t denominator of 10^9
  for (k=0; k<32; ++k)
  {
    int d = theta >= 0 ? 0 : -1;
    int tx = x - (((y>>k) ^ d) - d);
    int ty = y + (((x>>k) ^ d) - d);
    theta = theta - ((cordic_ctab[k] ^ d) - d);
    x = tx; y = ty;
  }  
  printf("Ans=(%i,%i)/10^9", x, y);
  return x;
}



int Table_Generate() 
{
  double theta = 1.0;
  int i, iterations = 32;
  for (i=0; i<iterations; i++)
    {
      cordic_ctab[i] = (int)(0.5 + atan(theta) * 1e9);
      printf("Cordic table %d is arctan %1.17f = %1.17f\n", i, theta, cordic_ctab[i]);
      //printf("%i,", cordic_ctab[i]);
      theta = theta / 2.0;
    }
}

int main()
{
  //Table_Generate() ;

  int v;
  for (v=0; v<2; v++) // Run a test in each iteration of this loop.
    {
      double theta = (v) ? 0.11: 1.22;
      printf(" : Cordic C :  %f ->    %f  (correct is %f)\n", theta, cordic(theta * 1e9) *1e-9, cos(theta));
    }
  return 0;
}


// eof
