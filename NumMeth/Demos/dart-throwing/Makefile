#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#
# Dart Throwing

include ../Makefile.inc


all:obj/DartThrowing.obj
	cd obj ; ulimit -t 15;	mono ./DartThrowing.exe



obj/DartThrowing.obj:DartThrowing.cs
	$(CSC)  DartThrowing.cs
	mkdir -p obj
	mv DartThrowing.exe obj




# eof

