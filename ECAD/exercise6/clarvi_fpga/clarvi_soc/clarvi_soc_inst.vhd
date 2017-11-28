	component clarvi_soc is
		port (
			clk_clk                                          : in  std_logic                     := 'X';             -- clk
			displaybuttons_pio_in_external_connection_export : in  std_logic_vector(15 downto 0) := (others => 'X'); -- export
			leds_pio_out_external_connection_export          : out std_logic_vector(9 downto 0);                     -- export
			left_dial_pio_in_external_connection_export      : in  std_logic_vector(7 downto 0)  := (others => 'X'); -- export
			pixelstream_0_conduit_end_0_lcd_red              : out std_logic_vector(7 downto 0);                     -- lcd_red
			pixelstream_0_conduit_end_0_lcd_green            : out std_logic_vector(7 downto 0);                     -- lcd_green
			pixelstream_0_conduit_end_0_lcd_blue             : out std_logic_vector(7 downto 0);                     -- lcd_blue
			pixelstream_0_conduit_end_0_lcd_hsync            : out std_logic;                                        -- lcd_hsync
			pixelstream_0_conduit_end_0_lcd_vsync            : out std_logic;                                        -- lcd_vsync
			pixelstream_0_conduit_end_0_lcd_de               : out std_logic;                                        -- lcd_de
			pixelstream_0_conduit_end_0_lcd_dclk             : out std_logic;                                        -- lcd_dclk
			pixelstream_0_conduit_end_0_lcd_dclk_en          : out std_logic;                                        -- lcd_dclk_en
			reset_reset_n                                    : in  std_logic                     := 'X';             -- reset_n
			right_dial_pio_in_external_connection_export     : in  std_logic_vector(7 downto 0)  := (others => 'X'); -- export
			eightbitstosevenseg_0_led_pins_led0              : out std_logic_vector(6 downto 0);                     -- led0
			eightbitstosevenseg_0_led_pins_led1              : out std_logic_vector(6 downto 0);                     -- led1
			eightbitstosevenseg_1_led_pins_led0              : out std_logic_vector(6 downto 0);                     -- led0
			eightbitstosevenseg_1_led_pins_led1              : out std_logic_vector(6 downto 0);                     -- led1
			eightbitstosevenseg_2_led_pins_led0              : out std_logic_vector(6 downto 0);                     -- led0
			eightbitstosevenseg_2_led_pins_led1              : out std_logic_vector(6 downto 0)                      -- led1
		);
	end component clarvi_soc;

	u0 : component clarvi_soc
		port map (
			clk_clk                                          => CONNECTED_TO_clk_clk,                                          --                                       clk.clk
			displaybuttons_pio_in_external_connection_export => CONNECTED_TO_displaybuttons_pio_in_external_connection_export, -- displaybuttons_pio_in_external_connection.export
			leds_pio_out_external_connection_export          => CONNECTED_TO_leds_pio_out_external_connection_export,          --          leds_pio_out_external_connection.export
			left_dial_pio_in_external_connection_export      => CONNECTED_TO_left_dial_pio_in_external_connection_export,      --      left_dial_pio_in_external_connection.export
			pixelstream_0_conduit_end_0_lcd_red              => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_red,              --               pixelstream_0_conduit_end_0.lcd_red
			pixelstream_0_conduit_end_0_lcd_green            => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_green,            --                                          .lcd_green
			pixelstream_0_conduit_end_0_lcd_blue             => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_blue,             --                                          .lcd_blue
			pixelstream_0_conduit_end_0_lcd_hsync            => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_hsync,            --                                          .lcd_hsync
			pixelstream_0_conduit_end_0_lcd_vsync            => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_vsync,            --                                          .lcd_vsync
			pixelstream_0_conduit_end_0_lcd_de               => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_de,               --                                          .lcd_de
			pixelstream_0_conduit_end_0_lcd_dclk             => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_dclk,             --                                          .lcd_dclk
			pixelstream_0_conduit_end_0_lcd_dclk_en          => CONNECTED_TO_pixelstream_0_conduit_end_0_lcd_dclk_en,          --                                          .lcd_dclk_en
			reset_reset_n                                    => CONNECTED_TO_reset_reset_n,                                    --                                     reset.reset_n
			right_dial_pio_in_external_connection_export     => CONNECTED_TO_right_dial_pio_in_external_connection_export,     --     right_dial_pio_in_external_connection.export
			eightbitstosevenseg_0_led_pins_led0              => CONNECTED_TO_eightbitstosevenseg_0_led_pins_led0,              --            eightbitstosevenseg_0_led_pins.led0
			eightbitstosevenseg_0_led_pins_led1              => CONNECTED_TO_eightbitstosevenseg_0_led_pins_led1,              --                                          .led1
			eightbitstosevenseg_1_led_pins_led0              => CONNECTED_TO_eightbitstosevenseg_1_led_pins_led0,              --            eightbitstosevenseg_1_led_pins.led0
			eightbitstosevenseg_1_led_pins_led1              => CONNECTED_TO_eightbitstosevenseg_1_led_pins_led1,              --                                          .led1
			eightbitstosevenseg_2_led_pins_led0              => CONNECTED_TO_eightbitstosevenseg_2_led_pins_led0,              --            eightbitstosevenseg_2_led_pins.led0
			eightbitstosevenseg_2_led_pins_led1              => CONNECTED_TO_eightbitstosevenseg_2_led_pins_led1               --                                          .led1
		);

