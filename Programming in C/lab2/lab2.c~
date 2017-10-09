#include <stdio.h>
#include "revwords.h"

static char strings[][80] = {
  "University",
  "University of Cambridge",
  "Hello, world",
  " Hello ",
  "University of Cambridge!",
  " University of Cambridge",
  "-$$$!!!-",
  "University-of-Cambridge"
};

int main(void) { 
  for (int i = 0; i < 8; i++) {
    printf("Original: %s\n", strings[i]);
    reverse_words(strings[i]);
    printf("Reversed: %s\n", strings[i]);
  }
  return 0;
}

/* This should result in the following output:

Original: University
Reversed: ytisrevinU
Original: University of Cambridge
Reversed: ytisrevinU fo egdirbmaC
Original: Hello, world
Reversed: olleH, dlrow
Original:  Hello 
Reversed:  olleH 
Original: University of Cambridge!
Reversed: ytisrevinU fo egdirbmaC!
Original:  University of Cambridge
Reversed:  ytisrevinU fo egdirbmaC
Original: -$$$!!!-
Reversed: -$$$!!!-
Original: University-of-Cambridge
Reversed: ytisrevinU-fo-egdirbmaC

*/


  
  
