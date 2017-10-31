	rotary_hex u0 (
		.clk_clk                                   (<connected-to-clk_clk>),                                   //                            clk.clk
		.eightbitstosevenseg_0_led_pins_led0       (<connected-to-eightbitstosevenseg_0_led_pins_led0>),       // eightbitstosevenseg_0_led_pins.led0
		.eightbitstosevenseg_0_led_pins_led1       (<connected-to-eightbitstosevenseg_0_led_pins_led1>),       //                               .led1
		.reset_reset_n                             (<connected-to-reset_reset_n>),                             //                          reset.reset_n
		.rotaryctl_0_rotary_event_rotary_cw        (<connected-to-rotaryctl_0_rotary_event_rotary_cw>),        //       rotaryctl_0_rotary_event.rotary_cw
		.rotaryctl_0_rotary_event_rotary_ccw       (<connected-to-rotaryctl_0_rotary_event_rotary_ccw>),       //                               .rotary_ccw
		.rotaryctl_0_rotary_in_rotary_in           (<connected-to-rotaryctl_0_rotary_in_rotary_in>),           //          rotaryctl_0_rotary_in.rotary_in
		.eightbitstosevenseg_1_led_pins_led0       (<connected-to-eightbitstosevenseg_1_led_pins_led0>),       // eightbitstosevenseg_1_led_pins.led0
		.eightbitstosevenseg_1_led_pins_led1       (<connected-to-eightbitstosevenseg_1_led_pins_led1>),       //                               .led1
		.rotaryctl_1_rotary_in_rotary_in           (<connected-to-rotaryctl_1_rotary_in_rotary_in>),           //          rotaryctl_1_rotary_in.rotary_in
		.rotaryctl_1_rotary_event_rotary_cw        (<connected-to-rotaryctl_1_rotary_event_rotary_cw>),        //       rotaryctl_1_rotary_event.rotary_cw
		.rotaryctl_1_rotary_event_rotary_ccw       (<connected-to-rotaryctl_1_rotary_event_rotary_ccw>),       //                               .rotary_ccw
		.shiftregctl_0_buttons_export              (<connected-to-shiftregctl_0_buttons_export>),              //          shiftregctl_0_buttons.export
		.shiftregctl_0_shiftreg_ext_shiftreg_clk   (<connected-to-shiftregctl_0_shiftreg_ext_shiftreg_clk>),   //     shiftregctl_0_shiftreg_ext.shiftreg_clk
		.shiftregctl_0_shiftreg_ext_shiftreg_loadn (<connected-to-shiftregctl_0_shiftreg_ext_shiftreg_loadn>), //                               .shiftreg_loadn
		.shiftregctl_0_shiftreg_ext_shiftreg_out   (<connected-to-shiftregctl_0_shiftreg_ext_shiftreg_out>)    //                               .shiftreg_out
	);

