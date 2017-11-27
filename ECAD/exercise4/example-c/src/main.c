#include "avalon_interface.h"

void debug_print(int value) {
	asm ("csrw	0x7B2, %0" : : "r" (value) );
}

void main(void)
{
    while (1) {
        int left;
	    left = avalon_read(PIO_ROTARY_L);
	    debug_print(left);
	    
	    vid_set_pixel(3, 3, PIXEL_BLUE);
	}
}
