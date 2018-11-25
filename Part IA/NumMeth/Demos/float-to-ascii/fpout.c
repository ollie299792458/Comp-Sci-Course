//
//

//* fpout.C
//* C Version of the floating point to ASCII conversion function.
//* (C) 2014 DJ Greaves, University of Cambridge, Computer Laboratory.
//*


#include <stdio.h>

int tens_table[]  = { 1,10,100,1000,10000,100000 };



void bin2ascii(unsigned int d0)
{
 
  // let fun scanup p = if Array.sub(tens_table, p) > d0 then p-1 else scanup(p+1)
  int p0 = 0;
  while (tens_table[p0] <= d0) p0 ++;
  p0 --;

   int ptr = 0;
   //fun digits d0 p =
   //            if p<0 then [] else
   //            let val d = d0 div Array.sub(tens_table, p)
   //                val r = d0 - d * Array.sub(tens_table, p)
   //            in chr(48 + d) :: digits r (p-1) end
   //   in digits d0 p0 end

   while(p0>=0) // Exercise: fix the printing of the case that does not work!
   {
     //printf("p0=%i d0=%i\n", p0, d0);
     int d = d0 / tens_table[p0];
     d0 = d0 - d * tens_table[p0];
     putchar((char)d + '0');
     p0 --;
   }
}



const int N_BIN_POWERS_OF_TEN = 6; // Range: 10**(+/- 63)


int dec32_table[] = { 1,10,100,1000,10000,100000 };

const long double bin_exponents_table[] =        { 1.0e1, 1.0e2, 1.0e4, 1.0e8, 1.0e16, 1.0e32 };

const long double bin_fracs_table[] =            { 1.0e-1, 1.0e-2, 1.0e-4, 1.0e-8, 1.0e-16, 1.0e-32 };

// The C version is a little richer and a little longer: it prints NaN or Inf as described in the next section of these notes.

void doubleToStr(int scientific_form, double d0, int precision)
{
  if (d0 < 0.0)
    { d0 = -d0;
      putchar('-');
    }
  union tobits
  { // Abuse union safety to get bits.
    double df;
    unsigned long long int di;
  } to;
  to.df = d0;
  int in_exp = (to.di >> 52) & 0x7FF;
  int mant = to.di & ((1llu<<52)-1);
  if (in_exp == 0x7FF)
    {
      if (mant == 0)
	{ putchar('I');          putchar('n');           putchar('f');
        }
      else
        { putchar('N');          putchar('a');           putchar('N');
        }
      return;
    }
  // Render all denormals as 0.0
  if (in_exp == 0)
    { putchar('0');
      putchar('.');
      putchar('0');
      return;
    }

  int exponent = 0;
  unsigned int i, bitpos = 1U<<(N_BIN_POWERS_OF_TEN-1);
  int lower_bound = tens_table[precision];
  int upper_bound = tens_table[precision+1];
  if (d0 < lower_bound)
    { for(i=N_BIN_POWERS_OF_TEN-1, 
	    bitpos = 1U<<(N_BIN_POWERS_OF_TEN-1);
	  bitpos; i--, bitpos>>=1)
	{ double q;
	  q = d0 * bin_exponents_table[i];
        if (q < upper_bound) 
          {
            d0 = q;
            exponent -= bitpos;
          }
	}
    }
  else
    { for(i=N_BIN_POWERS_OF_TEN-1,
	    bitpos = 1U<<(N_BIN_POWERS_OF_TEN-1);
	  bitpos; i--, bitpos>>=1)
	{ double q;
	  q = d0 * bin_fracs_table[i];
	  if (q >= lower_bound)
	    { d0 = q;
	      exponent += bitpos;
	    }
	}
    }
  exponent += precision;

  int imant = (int)d0;
  
  // Decimal point will only move a certain distance:
  // outside that range force scientific form.
  if (exponent> precision || exponent <0)
    scientific_form = 1;  
  
  int p, enable_trailzero_supress = 0;
  for (p = precision; p >= 0; p--)
    { int d = 0;
      while (imant>=dec32_table[p])
        { imant -= dec32_table[p];
          d++;
        }
      putchar(d + '0');
      if (enable_trailzero_supress && imant == 0) break;
      if (p == precision + (scientific_form ? 0: -exponent)) 
        { putchar('.'); // Print decimal point.
          enable_trailzero_supress = 1;
        }
    }
  if (scientific_form) // Print the exponent
    { putchar('e');
      bin2ascii(exponent);
    }
}



int main()
{
  doubleToStr(1, 13.45678, 2); printf("\n");

  doubleToStr(0, 13.45678, 3); printf("\n");
  return 0;
}

// eof
