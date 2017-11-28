
module clarvi_soc (
	clk_clk,
	displaybuttons_pio_in_external_connection_export,
	leds_pio_out_external_connection_export,
	left_dial_pio_in_external_connection_export,
	pixelstream_0_conduit_end_0_lcd_red,
	pixelstream_0_conduit_end_0_lcd_green,
	pixelstream_0_conduit_end_0_lcd_blue,
	pixelstream_0_conduit_end_0_lcd_hsync,
	pixelstream_0_conduit_end_0_lcd_vsync,
	pixelstream_0_conduit_end_0_lcd_de,
	pixelstream_0_conduit_end_0_lcd_dclk,
	pixelstream_0_conduit_end_0_lcd_dclk_en,
	reset_reset_n,
	right_dial_pio_in_external_connection_export,
	eightbitstosevenseg_0_led_pins_led0,
	eightbitstosevenseg_0_led_pins_led1,
	eightbitstosevenseg_1_led_pins_led0,
	eightbitstosevenseg_1_led_pins_led1,
	eightbitstosevenseg_2_led_pins_led0,
	eightbitstosevenseg_2_led_pins_led1);	

	input		clk_clk;
	input	[15:0]	displaybuttons_pio_in_external_connection_export;
	output	[9:0]	leds_pio_out_external_connection_export;
	input	[7:0]	left_dial_pio_in_external_connection_export;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_red;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_green;
	output	[7:0]	pixelstream_0_conduit_end_0_lcd_blue;
	output		pixelstream_0_conduit_end_0_lcd_hsync;
	output		pixelstream_0_conduit_end_0_lcd_vsync;
	output		pixelstream_0_conduit_end_0_lcd_de;
	output		pixelstream_0_conduit_end_0_lcd_dclk;
	output		pixelstream_0_conduit_end_0_lcd_dclk_en;
	input		reset_reset_n;
	input	[7:0]	right_dial_pio_in_external_connection_export;
	output	[6:0]	eightbitstosevenseg_0_led_pins_led0;
	output	[6:0]	eightbitstosevenseg_0_led_pins_led1;
	output	[6:0]	eightbitstosevenseg_1_led_pins_led0;
	output	[6:0]	eightbitstosevenseg_1_led_pins_led1;
	output	[6:0]	eightbitstosevenseg_2_led_pins_led0;
	output	[6:0]	eightbitstosevenseg_2_led_pins_led1;
endmodule
