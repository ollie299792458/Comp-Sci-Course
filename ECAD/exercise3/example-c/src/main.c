#include "division.h"
#include "cycles.h"
#define TICKS_PER_CENTI_SECOND 10

void hex_output(int value)
{
	int *hex_leds = (int *) 0x04000080;  // define a pointer to the register
	*hex_leds = value;                   // write the value to that address
}

void debug_print(int value)
{
	asm ("csrw	0x7B2, %0" : : "r" (value) );
}

int get_binary_coded_time() {
    int time = get_time();
	time = div(time, TICKS_PER_CENTI_SECOND);
	
	int value = rem(time, 100);
	time = div(time, 100);
	
	value += rem(time, 60) << (2*4);
	time = div(time, 60);
	
	value += rem(time, 60) << (4*4);
	time = div(time, 60);

	return value;
}

int main(void)
{
	while (1) {
	    int value = get_binary_coded_time();	
	    hex_output(value);
	}
}
