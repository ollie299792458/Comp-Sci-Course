#include <float.h>
#include <stdio.h>


void binprintf(double f)
{
  union { float f; unsigned long long int i; } k;
  k.f = f;
  int i; for (i=0;i<32;i++) { putchar((k.i>>31) & 1? '1':'0'); k.i <<= 1; }
  putchar('\n');
}


void binprintd(double d)
{
  union { double d; unsigned long long int i; } k;
  k.d = d;
  int i; for (i=0;i<64;i++) { putchar((k.i>>63) & 1? '1':'0'); k.i <<= 1; }
  putchar('\n');
}


/* Show the dangers of counting with inexact floating point values --   */
/* (accidentally) double gets it `wrong' and float gets it `right'.     */

int main()
{  const double sixth = 1.0/6.0;
   volatile double d;
   volatile float f;
   for (f = 0.0; f < 1.0; f += sixth) { printf("Here with float %.7f    ", f); binprintf(f); }
   putchar('\n');
   for (d = 0.0; d < 1.0; d += sixth) { printf("Here with double %.16f  ", d); binprintd(d); }
   return 0;
}
