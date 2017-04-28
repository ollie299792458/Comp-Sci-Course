#include <stdio.h>



/* this program shows how you can convert a +ve 'int' to a double by    */
/* only using bit operations.                                           */


int main() 
{
  union { double d; int w[2]; } u;
  u.d = 4.0 * 1024 * 1024 * 1024 * 1024 * 1024;   /* put 2^52 in d */
  printf("u.d = %.3f\n", u.d);
      /* testing only: */
      /* this means lsb represeents 1, so increment it to check */
      u.w[0]++;
      printf("u.d (+1 in lsb) = %.3f\n", u.d);
  /* now put 42 in lsbits of mantissa, and subtract the 2^52 again */
  u.w[0] = 42;
  u.d -= 4.0 * 1024 * 1024 * 1024 * 1024 * 1024;
  printf("u.d (should now by 42) = %.3f\n", u.d);
  return 0;
}


// eof
