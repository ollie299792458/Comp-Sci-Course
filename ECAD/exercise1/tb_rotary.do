# ModelSim do script to test rotary.sv using tb_rotary.sv
#
# set up the "work" library
vlib work
# compile our SystemVerilog files
vlog debounce.sv
vlog rotary.sv
vlog tb_rotary.sv
# point the simulator at the compiled design
vsim work.tb_rotary
# run simulation for 200 million cycles
run 200000000
quit
