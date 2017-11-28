	component clarvi_soc is
		port (
			clk_clk                                       : in  std_logic                     := 'X';             -- clk
			hex_digits_pio_out_external_connection_export : out std_logic_vector(23 downto 0);                    -- export
			leds_pio_out_external_connection_export       : out std_logic_vector(9 downto 0);                     -- export
			pixelstream_0_conduit_end_0_lcd_red           : out std_logic_vector(7 downto 0);                     -- lcd_red
			pixelstream_0_conduit_end_0_lcd_green         : out std_logic_vector(7 downto 0);                     -- lcd_green
			pixelstream_0_conduit_end_0_lcd_blue          : out std_logic_vector(7 downto 0);                     -- lcd_blue
			pixelstream_0_conduit_end_0_lcd_hsync         : out std_logic;                                        -- lcd_hsync
			pixelstream_0_conduit_end_0_lcd_vsync         : out std_logic;                                        -- lcd_vsync
			pixelstream_0_conduit_end_0_lcd_de            : out std_logic;                                        -- lcd_de
			pixelstream_0_conduit_end_0_lcd_dclk          : out std_logic;                                        -- lcd_dclk
			pixelstream_0_conduit_end_0_lcd_dclk_en       : out std_logic;                                        -- lcd_dclk_en
			reset_reset_n                                 : in  std_logic                     := 'X';             -- reset_n
			rotaryctl_0_rotary_event_rotary_cw            : out std_logic;                                        -- rotary_cw
			rotaryctl_0_rotary_event_rotary_ccw           : out std_logic;                                        -- rotary_ccw
			rotaryctl_1_rotary_event_rotary_cw            : out std_logic;                                        -- rotary_cw
			rotaryctl_1_rotary_event_rotary_ccw           : out std_logic;                                        -- rotary_ccw
			shiftregctl_0_shiftreg_ext_shiftreg_clk       : out std_logic;                                        -- shiftreg_clk
			shiftregctl_0_shiftreg_ext_shiftreg_loadn     : out std_logic;                                        -- shiftreg_loadn
			shiftregctl_0_shiftreg_ext_shiftreg_out       : in  std_logic                     := 'X';             -- shiftreg_out
			rotaryctl_right_rotary_in_export              : in  std_logic_vector(1 downto 0)  := (others => 'X'); -- export
			rotaryctl_left_rotary_in_export               : in  std_logic_vector(1 downto 0)  := (others => 'X')  -- export
		);
	end component clarvi_soc;

	u0 : component clarvi_soc
		port map (
			clk_clk                                       => CONNECTED_TO_clk_clk,                                       --                                    clk.clk
			hex_digits_pio_out_external_connection_export => CONNECTED_TO_hex_digits_pio_out_external_connection_export, -- hex_digits_pio_out_external_connection.export
			leds_pio_out_external_connection_export       => CONNECTED_TO_leds_pio_out_external_connection_export,       --       leds_pio_out_external_connection.export
			pixelstream_0_conduit_end_0_lcd_red           => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_red,           --            pixelstream_0_conduit_end_0.lcd_red
			pixelstream_0_conduit_end_0_lcd_green         => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_green,         --                                       .lcd_green
			pixelstream_0_conduit_end_0_lcd_blue          => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_blue,          --                                       .lcd_blue
			pixelstream_0_conduit_end_0_lcd_hsync         => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_hsync,         --                                       .lcd_hsync
			pixelstream_0_conduit_end_0_lcd_vsync         => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_vsync,         --                                       .lcd_vsync
			pixelstream_0_conduit_end_0_lcd_de            => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_de,            --                                       .lcd_de
			pixelstream_0_conduit_end_0_lcd_dclk          => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_dclk,          --                                       .lcd_dclk
			pixelstream_0_conduit_end_0_lcd_dclk_en       => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_dclk_en,       --                                       .lcd_dclk_en
			reset_reset_n                                 => CONNECTED_TO_reset_reset_n,                                 --                                  reset.reset_n
			rotaryctl_0_rotary_event_rotary_cw            => CONNECTED_TO_rotaryctl_0_rotary_event_rotary_cw,            --               rotaryctl_0_rotary_event.rotary_cw
			rotaryctl_0_rotary_event_rotary_ccw           => CONNECTED_TO_rotaryctl_0_rotary_event_rotary_ccw,           --                                       .rotary_ccw
			rotaryctl_1_rotary_event_rotary_cw            => CONNECTED_TO_rotaryctl_1_rotary_event_rotary_cw,            --               rotaryctl_1_rotary_event.rotary_cw
			rotaryctl_1_rotary_event_rotary_ccw           => CONNECTED_TO_rotaryctl_1_rotary_event_rotary_ccw,           --                                       .rotary_ccw
			shiftregctl_0_shiftreg_ext_shiftreg_clk       => CONNECTED_TO_shiftregctl_0_shiftreg_ext_shiftreg_clk,       --             shiftregctl_0_shiftreg_ext.shiftreg_clk
			shiftregctl_0_shiftreg_ext_shiftreg_loadn     => CONNECTED_TO_shiftregctl_0_shiftreg_ext_shiftreg_loadn,     --                                       .shiftreg_loadn
			shiftregctl_0_shiftreg_ext_shiftreg_out       => CONNECTED_TO_shiftregctl_0_shiftreg_ext_shiftreg_out,       --                                       .shiftreg_out
			rotaryctl_right_rotary_in_export              => CONNECTED_TO_rotaryctl_right_rotary_in_export,              --              rotaryctl_right_rotary_in.export
			rotaryctl_left_rotary_in_export               => CONNECTED_TO_rotaryctl_left_rotary_in_export                --               rotaryctl_left_rotary_in.export
		);

