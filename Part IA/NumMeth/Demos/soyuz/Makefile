#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#
# FDTD simulations - Soyuz Rocket




include ../Makefile.inc

PNAME=soyuz
all:runold

 
soyuz-run:soyuz.ml
	ocamlc -o soyuz-run graphics.cma unix.cma soyuz.ml

runold: soyuz-run
	./soyuz-run


# eof







