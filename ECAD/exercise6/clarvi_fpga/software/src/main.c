#include "avalon_interface.h"

void debug_print(int value) {
	asm ("csrw	0x7B2, %0" : : "r" (value) );
}

void full_screen(int colour) {
    for (int x = 0; x < DISPLAY_WIDTH; x++) {
        for (int y = 0; y < DISPLAY_HEIGHT; y++) {
            vid_set_pixel(x,y,colour);
        }
    }
}

int calculate_change(int new, int old) {
    if (old == 8 && new == 0) {
        return 1;
    }
    if (old == 0 && new == 8) {
        return -1;
    }
    if (new-old > 0) {
        return 1;
    }
    if (new-old < 0) {
        return -1;
    }
    return 0;
}

int wrap_around(int value, int max) {
    if (value < 0) {
        return max;
    }
    if (value > max) {
        return 0;
    }
    return value;
}

void main(void) {
    int x = DISPLAY_WIDTH/2, y = DISPLAY_HEIGHT/2;
    int lastleft = 0, lastright = 0;
    while (1) {
	    if ((BUTTONS_MASK_DIALL_CLICK | BUTTONS_MASK_DIALR_CLICK) & avalon_read(PIO_BUTTONS)) {
	        full_screen(PIXEL_BLACK);
	    }
    
        int left;
	    left = avalon_read(PIO_ROTARY_L);
        int right;
	    right = avalon_read(PIO_ROTARY_R);
	    
	    x += calculate_change(left, lastleft);
	    y += calculate_change(right, lastright);
	 
	    x = wrap_around(x, DISPLAY_WIDTH);
	    y = wrap_around(y, DISPLAY_HEIGHT);   
	    
	    vid_set_pixel(x, y, PIXEL_WHITE);
	    lastleft = left;
	    lastright = right;
	}
}
