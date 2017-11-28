create_clock -period 20 -waveform {0 10} CLOCK_50
create_clock -period 20 -waveform {0 10} CLOCK2_50
create_clock -period 20 -waveform {0 10} CLOCK3_50
create_clock -period 20 -waveform {0 10} CLOCK4_50
create_clock -period "9 MHZ" LCD_DCLK
derive_pll_clocks
derive_clock_uncertainty

# prevent timing failures from PixelStream where the video output is in a separate clock domain from the main system
set_false_path -to [get_ports {LCD_DCLK}]
set_false_path -from [get_keepers {*mkPixelStream:*:vp_sync|sDataSyncIn*}] -to [get_keepers {*|mkPixelStream:*|SyncRegister:vp_sync|dD_OUT*]}]
set_false_path -from [get_keepers {*mkPixelStream:*:pix_sync|sGEnqPtr*}] -to [get_keepers {*|mkPixelStream:*|SyncFIFO:pix_sync|dSyncReg1*}]
set_false_path -from [get_keepers {*mkPixelStream:*:vp_sync|SyncHandshake:sync|sToggleReg}] -to [get_keepers {*mkPixelStream:*|SyncRegister:vp_sync|SyncHandshake:sync|dSyncReg1}]
set_false_path -from [get_keepers {*mkPixelStream:*:pix_sync|dGDeqPtr*}] -to [get_keepers {*|mkPixelStream:pixelstream_0|SyncFIFO:pix_sync|sSyncReg1*}]
set_false_path -from [get_keepers {*mkPixelStream:*:vp_sync|SyncHandshake:sync|dLastState}] -to [get_keepers {*|mkPixelStream:*|SyncRegister:vp_sync|SyncHandshake:sync|sSyncReg1}]
set_false_path -to [get_pins -nocase -compatibility_mode {*|alt_rst_sync_uq1|altera_reset_synchronizer_int_chain*|clrn}]
set_false_path -from [get_keepers {*|altera_reset_controller:rst_controller|r_sync_rst}] -to [get_keepers {*|mkPixelStream:*|SyncRegister:vp_sync|dD_OUT*}]
#set_clock_groups -asynchronous \
#  -group [get_clocks {qsys0|pll|altera_pll_i|general[0].gpll~PLL_OUTPUT_COUNTER|divclk qsys0|pll|altera_pll_i|general[1].gpll~PLL_OUTPUT_COUNTER|divclk}] \
#  -group [get_clocks {qsys0|pll|altera_pll_i|general[2].gpll~PLL_OUTPUT_COUNTER|divclk}]

set_clock_groups -asynchronous \
  -group [get_clocks {qsys0|pll|altera_pll_i|general[0].gpll~PLL_OUTPUT_COUNTER|divclk}] \
  -group [get_clocks {qsys0|pll|altera_pll_i|general[1].gpll~PLL_OUTPUT_COUNTER|divclk}]
