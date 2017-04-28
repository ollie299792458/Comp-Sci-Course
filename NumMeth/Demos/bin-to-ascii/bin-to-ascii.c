// Integer Output - binary to ASCII characters.
//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015. 

#include <stdio.h>

int tens_table[]  = { 1,10,100,1000,10000,100000 };


char buffer[32];


char *bin2ascii(unsigned int d0)
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
     buffer[ptr++] = (char)d + '0';
     p0 --;
   }
  buffer[ptr] = (char) 0;
  return buffer;
}



int main()
{
  char *ans = bin2ascii(12000+345);
  printf("the answer is %s\n", ans);
  return 0;
}

// eof
