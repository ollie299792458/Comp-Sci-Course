#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# 
# (* (C) 2014 DJ Greaves, University of Cambridge.                          *)

# Verhulst's logistic map - an iteration that just jumps around a lot.
#



#
#
#
include ../Makefile.inc

all: runML

PNAME=verhulst

plot:
	nohup	gnuplot --persist -e "plot [-0.5:1.5] x, 4*x*(1-x) with lines"	



runML:
	$(SMLRUN) < $(PNAME).ml



# eof
