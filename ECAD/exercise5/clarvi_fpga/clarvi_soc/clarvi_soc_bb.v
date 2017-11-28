
module clarvi_soc (
	clk_clk,
	pixelstream_0_conduit_end_0_lcd_red,
	pixelstream_0_conduit_end_0_lcd_green,
	pixelstream_0_conduit_end_0_lcd_blue,
	pixelstream_0_conduit_end_0_lcd_hsync,
	pixelstream_0_conduit_end_0_lcd_vsync,
	pixelstream_0_conduit_end_0_lcd_de,
	pixelstream_0_conduit_end_0_lcd_dclk,
	pixelstream_0_conduit_end_0_lcd_dclk_en,
	reset_reset_n,
	leds_pio_out_external_connection_export,
	hex_digits_pio_out_external_connection_export,
	left_dial_pio_in_external_connection_export,
	right_dial_pio_in_external_connection_export,
	displaybuttons_pio_in_external_connection_export);	

	input		clk_clk;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_red;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_green;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_blue;
	output		pixelstream_0_conduit_end_0_lcd_hsync;
	output		pixelstream_0_conduit_end_0_lcd_vsync;
	output		pixelstream_0_conduit_end_0_lcd_de;
	output		pixelstream_0_conduit_end_0_lcd_dclk;
	output		pixelstream_0_conduit_end_0_lcd_dclk_en;
	input		reset_reset_n;
	output	[9:0]	leds_pio_out_external_connection_export;
	output	[23:0]	hex_digits_pio_out_external_connection_export;
	input	[7:0]	left_dial_pio_in_external_connection_export;
	input	[7:0]	right_dial_pio_in_external_connection_export;
	input	[15:0]	displaybuttons_pio_in_external_connection_export;
endmodule
