# start a new library
vlib work
vmap work work

# determine whether we're on a 32 or 64 bit Modelsim...
set MODEL_TECH $::env(MODEL_TECH)
set sixtyfour [string first "64" $MODEL_TECH]
if {$sixtyfour == "-1"} {
	set ARCH "-m32"
} else {
	set ARCH "-m64"
}

# ... and compile the Yarvi Avalon emulator as an appropriate kind of shared library
exec gcc -shared $ARCH -fPIC -I $MODEL_TECH/../include/ -o clarvi_avalon_external.so clarvi_avalon_external.c

vlog clarvi_avalon_external.sv

if {$argc == 2} {
    vlog clarvi.sv +define+$2
} else {
    vlog clarvi.sv
}

vlog bram.sv
vlog clarvi_avalon.sv
vlog clarvi_sim.sv

# link the compiled Verilog with the C shared library into a simulator
#vsim -novopt -sv_lib clarvi_avalon_external work.clarvi_sim
if {$argc > 0} {
    vsim  -novopt -sv_lib clarvi_avalon_external work.clarvi_sim -GINIT_FILE=$1 -t ns -voptargs=+acc=npr
} else {
    vsim  -novopt -sv_lib clarvi_avalon_external work.clarvi_sim -t ns -voptargs=+acc=npr
}

# add signals to the waveform viewer
add wave -position insertpoint  \
sim:/clarvi_sim/clock \
sim:/clarvi_sim/reset \
sim:/clarvi_sim/main_address \
sim:/clarvi_sim/main_read \
sim:/clarvi_sim/main_readdatavalid \
sim:/clarvi_sim/main_write \
sim:/clarvi_sim/main_byteenable \
sim:/clarvi_sim/clarvi/clarvi/ex_next_pc \
sim:/clarvi_sim/clarvi/clarvi/if_pc \
sim:/clarvi_sim/clarvi/clarvi/de_ex_instr

