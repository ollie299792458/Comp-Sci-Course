//#  Loss of significance demo.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.



#include <stdio.h>

int main()
{   int i;
    double x = 1.0/9.0;
    x *= 10;
    for (i=0; i<30; i++)
    {   printf("%e\n", x);
        x = (x - 1) * 10;   // C treats as (x-1.0) * 10.0
    }
    return 0;
}

// eof
