#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#
# Linear FDTD simulations

# First-order system
# 1. FDTD simulation of heat conduction or RC network (no inductance)

# Second-order system:
# 2. FDTD simulation of a violin string - has velocity and position in state vector.



include ../Makefile.inc

PNAME1=RC-heatflow-1d
PNAME2=ViolinStringFDTD

all:p2

p1:PlotUtil.dll
	$(CSC) -r:PlotUtil.dll $(PNAME1).cs
	mono $(PNAME1).exe

p2:PlotUtil.dll
	$(CSC) -r:PlotUtil.dll $(PNAME2).cs
	mono $(PNAME2).exe


PlotUtil.dll:PlotUtil.cs
	$(CSC) /target:library PlotUtil.cs $(MLIBS) 

# eofa





