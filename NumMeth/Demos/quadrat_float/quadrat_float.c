//#  Quadratic equation solving - using an IF statement to avoid cancellation and loss of significance.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.


#include <stdio.h>
#include <math.h>

float root1(float a, float b, float c)
{   /* write explict "float" temporaries to stop C compiler using 'double'
    ** instead...  (logically) return (-b + sqrt(b*b - 4*a*c))/(2*a);
    */
    float t1 = b*b - 4*a*c;
    float t2 = sqrt(t1);
    float t3 = -b + t2;
    return t3/(2*a);
}

float root2(float a, float b, float c)
{   /* write explict "float" temporaries to stop C compiler using 'double'
    ** instead...  (logically) return (-b - sqrt(b*b - 4*a*c))/(2*a);
    */
    float t1 = b*b - 4*a*c;
    float t2 = sqrt(t1);
    float t3 = -b - t2;
    return t3/(2*a);
}

double droot1(double a, double b, double c)
{   return (-b + sqrt(b*b - 4*a*c))/(2*a);   }
double droot2(double a, double b, double c)
{   return (-b - sqrt(b*b - 4*a*c))/(2*a);   }

void test(float a,float b,float c)
{
     float r1 = root1(a,b,c);
     float r2 = root2(a,b,c);
     double dr1 = droot1(a,b,c);
     double dr2 = droot2(a,b,c);
     printf("%e x^2 + %e x + %e =>\nroot1 %e (or %e)\nroot2 %e (or %e)\n\n",
            a,b,c, r1,dr1 ,r2,dr2);
}

int main()
{    test(1,-3,2);
     test(1,10,1);
     test(1,100,1);
     test(1,1000,1);
     test(1,10000,1);
     return 0;
}
