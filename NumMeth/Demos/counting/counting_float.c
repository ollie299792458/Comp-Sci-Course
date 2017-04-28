#include <stdio.h>

/* what happens when we increment a 'float' forever...                  */

static void showval(float f)
{   union { float f; int n; } u;
    u.f = f;
    /* This use of 'union' is *not* portable, but it works on x86/gcc.  */
    printf("%.8e (0x%.8x or %d:%.6x)\n", f, u.n,
           u.n>>23 & 0xff, u.n & 0x7fffff);
}

int main()
{ float i=0,j;
  do { j=i; i++; } while (i != j);
  showval(i);
  return 0;
}

//eof
