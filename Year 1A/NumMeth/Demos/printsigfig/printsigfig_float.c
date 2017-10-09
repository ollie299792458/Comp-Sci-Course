#include <stdio.h>

//# Investigation of printing significant figures.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu


static void showval(float f)
{   union { float f; int n; } u;
    u.f = f;
    /* This use of 'union' is *not* portable, but it works on x86/gcc.  */
    printf("%.7e %.8e (0x%.8x or %d:%.6x)\n", f, f, u.n,
           u.n>>23 & 0xff, u.n & 0x7fffff);
}

int main()
{ union { float f; int n; } u;
  u.f = 99;
  showval(u.f);     // show 99.0
  u.n++;            // 99.0 with lsbit incremented, so 99.0000x
  showval(u.f);     // show it, noting how it affects the decimal.
  u.f = 127;        // Repeat for 127.0 and its neighbours
  showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);
  u.n++; showval(u.f);

  return 0;
}

// eof

