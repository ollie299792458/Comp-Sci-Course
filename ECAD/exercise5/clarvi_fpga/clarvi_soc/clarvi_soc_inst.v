	clarvi_soc u0 (
		.clk_clk                                          (<connected-to-clk_clk>),                                          //                                       clk.clk
		.pixelstream_0_conduit_end_0_lcd_red              (<connected-to-pixelstream_0_conduit_end_0_lcd_red>),              //               pixelstream_0_conduit_end_0.lcd_red
		.pixelstream_0_conduit_end_0_lcd_green            (<connected-to-pixelstream_0_conduit_end_0_lcd_green>),            //                                          .lcd_green
		.pixelstream_0_conduit_end_0_lcd_blue             (<connected-to-pixelstream_0_conduit_end_0_lcd_blue>),             //                                          .lcd_blue
		.pixelstream_0_conduit_end_0_lcd_hsync            (<connected-to-pixelstream_0_conduit_end_0_lcd_hsync>),            //                                          .lcd_hsync
		.pixelstream_0_conduit_end_0_lcd_vsync            (<connected-to-pixelstream_0_conduit_end_0_lcd_vsync>),            //                                          .lcd_vsync
		.pixelstream_0_conduit_end_0_lcd_de               (<connected-to-pixelstream_0_conduit_end_0_lcd_de>),               //                                          .lcd_de
		.pixelstream_0_conduit_end_0_lcd_dclk             (<connected-to-pixelstream_0_conduit_end_0_lcd_dclk>),             //                                          .lcd_dclk
		.pixelstream_0_conduit_end_0_lcd_dclk_en          (<connected-to-pixelstream_0_conduit_end_0_lcd_dclk_en>),          //                                          .lcd_dclk_en
		.reset_reset_n                                    (<connected-to-reset_reset_n>),                                    //                                     reset.reset_n
		.leds_pio_out_external_connection_export          (<connected-to-leds_pio_out_external_connection_export>),          //          leds_pio_out_external_connection.export
		.hex_digits_pio_out_external_connection_export    (<connected-to-hex_digits_pio_out_external_connection_export>),    //    hex_digits_pio_out_external_connection.export
		.left_dial_pio_in_external_connection_export      (<connected-to-left_dial_pio_in_external_connection_export>),      //      left_dial_pio_in_external_connection.export
		.right_dial_pio_in_external_connection_export     (<connected-to-right_dial_pio_in_external_connection_export>),     //     right_dial_pio_in_external_connection.export
		.displaybuttons_pio_in_external_connection_export (<connected-to-displaybuttons_pio_in_external_connection_export>)  // displaybuttons_pio_in_external_connection.export
	);

