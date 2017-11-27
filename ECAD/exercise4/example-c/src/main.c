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
    return new-old;
}

void main(void) {
    int x = 0, y = 0;
    int lastleft = 0, lastright = 0;
    while (1) {
        int clicked = 0;
	    clicked = clicked || (BUTTONS_MASK_DIALR_CLICK && avalon_read(PIO_BUTTONS));
	    clicked = clicked || (BUTTONS_MASK_DIALL_CLICK && avalon_read(PIO_BUTTONS));
	    if (clicked) {
	        full_screen(PIXEL_BLACK);
	    }
    
        int left;
	    left = avalon_read(PIO_ROTARY_L);
        int right;
	    right = avalon_read(PIO_ROTARY_R);
	    
	    x += calculate_change(left, lastleft);
	    y += calculate_change(right, lastright);
	    x++;
	    vid_set_pixel(x, y, PIXEL_WHITE);
	    lastleft = left;
	    lastright = right;
	}
}
