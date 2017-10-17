# ModelSim do script to test debounce.sv using tb_debounce.sv
#
# set up the "work" library
vlib work
# compile our SystemVerilog files
vlog debounce.sv
vlog tb_debounce.sv
# point the simulator at the compiled design
vsim work.tb_debounce
# run simulation for 100 million time units = 0.1s
run 100000000
quit
