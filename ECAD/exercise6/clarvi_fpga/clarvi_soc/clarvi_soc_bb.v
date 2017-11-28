
module clarvi_soc (
	clk_clk,
	hex_digits_pio_out_external_connection_export,
	leds_pio_out_external_connection_export,
	pixelstream_0_conduit_end_0_lcd_red,
	pixelstream_0_conduit_end_0_lcd_green,
	pixelstream_0_conduit_end_0_lcd_blue,
	pixelstream_0_conduit_end_0_lcd_hsync,
	pixelstream_0_conduit_end_0_lcd_vsync,
	pixelstream_0_conduit_end_0_lcd_de,
	pixelstream_0_conduit_end_0_lcd_dclk,
	pixelstream_0_conduit_end_0_lcd_dclk_en,
	reset_reset_n,
	rotaryctl_0_rotary_event_rotary_cw,
	rotaryctl_0_rotary_event_rotary_ccw,
	rotaryctl_1_rotary_event_rotary_cw,
	rotaryctl_1_rotary_event_rotary_ccw,
	shiftregctl_0_shiftreg_ext_shiftreg_clk,
	shiftregctl_0_shiftreg_ext_shiftreg_loadn,
	shiftregctl_0_shiftreg_ext_shiftreg_out,
	rotaryctl_right_rotary_in_export,
	rotaryctl_left_rotary_in_export);	

	input		clk_clk;
	output	[23:0]	hex_digits_pio_out_external_connection_export;
	output	[9:0]	leds_pio_out_external_connection_export;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_red;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_green;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_blue;
	output		pixelstream_0_conduit_end_0_lcd_hsync;
	output		pixelstream_0_conduit_end_0_lcd_vsync;
	output		pixelstream_0_conduit_end_0_lcd_de;
	output		pixelstream_0_conduit_end_0_lcd_dclk;
	output		pixelstream_0_conduit_end_0_lcd_dclk_en;
	input		reset_reset_n;
	output		rotaryctl_0_rotary_event_rotary_cw;
	output		rotaryctl_0_rotary_event_rotary_ccw;
	output		rotaryctl_1_rotary_event_rotary_cw;
	output		rotaryctl_1_rotary_event_rotary_ccw;
	output		shiftregctl_0_shiftreg_ext_shiftreg_clk;
	output		shiftregctl_0_shiftreg_ext_shiftreg_loadn;
	input		shiftregctl_0_shiftreg_ext_shiftreg_out;
	input	[1:0]	rotaryctl_right_rotary_in_export;
	input	[1:0]	rotaryctl_left_rotary_in_export;
endmodule
