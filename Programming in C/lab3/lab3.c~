#include <stdio.h>
#include <stdlib.h>
#include "list.h"

int triple(int x) { 
  return x + x + x;
}

int increment(int x) { 
  return x + 1;
}

int main(void) { 
  List *test = cons(5, cons(1, cons(3, cons(2, cons(4, NULL)))));
  print_list(test);

  iterate(triple, test);
  print_list(test);

  iterate(increment, test);
  print_list(test);

  int n = sum(test);
  printf("Sum = %d\n", n);

  /* The challenge problems -- uncomment this to run */
  /*
  test = mergesort(test);
  print_list(test);
  */

  return 0;
}
  
/* 
1. Expected output for base exercise:

$ ./lab3 
[5, 1, 3, 2, 4]
[15, 3, 9, 6, 12]
[16, 4, 10, 7, 13]
Sum = 50

2. Expected output with challenge enabled:

$ ./lab3 
[5, 1, 3, 2, 4]
[15, 3, 9, 6, 12]
[16, 4, 10, 7, 13]
Sum = 50
[4, 7, 10, 13, 16]

*/
