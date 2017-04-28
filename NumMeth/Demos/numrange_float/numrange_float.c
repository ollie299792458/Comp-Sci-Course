
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.
//  shows the range expressible as 'float' numbers, including dnorm/infinity 

#include <stdio.h>

/* shows the range expressible as 'float' numbers, including dnorm/infinity */

static void showval(float seed, int i, float f)
{   union { float f; int n; } u;
    u.f = f;
    /* This use of 'union' is *not* portable, but it works on x86/gcc.  */
    printf("%f * 2^%d = %e (0x%.8x or %d:%.6x)\n", seed, i, f, u.n,
           u.n>>23 & 0xff, u.n & 0x7fffff);
}

int main() {
  int i;
  float seed = 1.75;
  float f, fold;
  f = seed; i = 0;
  do
  {  showval(seed, i, f);
     i++;
     fold = f;
     f *= 2;
  } while (f != fold);
  printf("========\n");
  f = seed; i = 0;
  do
  {  showval(seed, i, f);
     i--;
     fold = f;
     f /= 2;
  } while (f != fold);
  printf("========\n");
  return 0;
}
