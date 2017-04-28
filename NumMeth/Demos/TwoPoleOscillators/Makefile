#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#
# Linear FDTD simulations


# Second-order system:
# 1. FDTD simulation of a two-pole ring oscillator.



include ../Makefile.inc

PNAME1=TwoPoleOscillator
PNAME2=

all:p1

p1:PlotUtil.dll
	$(CSC) -r:PlotUtil.dll $(PNAME1).cs
	mono $(PNAME1).exe

p2:PlotUtil.dll
	$(CSC) -r:PlotUtil.dll $(PNAME2).cs
	mono $(PNAME2).exe


PlotUtil.dll:../RC-heatflow-1d/PlotUtil.cs
	$(CSC) /target:library ../RC-heatflow-1d/PlotUtil.cs $(MLIBS) 
	cp ../RC-heatflow-1d/PlotUtil.dll .
# eofa





