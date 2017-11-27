#include "division.h"
#include "cycles.h"
#define TICKS_PER_CENTI_SECOND 1000

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
	
	int centiseconds = rem(time,100);
	time = div(time, 100);
	
	int value = rem(centiseconds, 10) << (0*4);
    value += div(centiseconds, 10) << (1*4);
	
	int seconds = rem(time, 60);
	time = div(time, 60);
	
	value += rem(seconds, 10) << (2*4);
	value += div(seconds, 10) << (3*4);
	int debug1 = rem(seconds,10);
	int debug2 = div(seconds,10);
	debug_print(debug1);
	debug_print(debug2);
	
	int minutes = rem(time,60);
	time = div(time, 60);
	
	value += rem(minutes, 10) << (4*4);
	value += div(minutes, 10) << (5*4);

	return value;
}

int main(void)
{
	while (1) {
	    int value = get_binary_coded_time();	
	    hex_output(value);
	    //debug
	    //debug_print(value);
	}
}
