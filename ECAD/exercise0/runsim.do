# set up the "work" library
vlib work
# compile our SystemVerilog files
vlog tlight.sv
vlog tb_tlight.sv
# point the simulator at the compiled design
vsim work.tb_tlight
# add waveforms to the "Wave" pane
add wave -position insertpoint \
/tb_tlight/r                 \
/tb_tlight/a                 \
/tb_tlight/g                 \
/tb_tlight/clk
# run simulation for 200 nanoseconds
run 200 ns
