#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#
# FDTD simulations

# WaterNetwork1 - Ad hoc coding, without using a matrix form.

# Later we will see how to code this up in flow matrix form giving MatrixWaterNetwork.
#


include ../Makefile.inc

PLOTUITL_DIR=../RC-heatflow-1d
SIMUSOLVE_DIR=../lu-decomposition
CLASSPATH=  -classpath .:$(SIMUSOLVE_DIR)::$(PLOTUTIL_DIR)


PNAME1=WaterNetwork1
PNAME2=MatrixWaterNetwork

all:p2

p1:PlotUtil.dll
	$(CSC) -r:PlotUtil.dll $(PNAME1).cs
	mono $(PNAME1).exe

p2:PlotUtil.dll SimuSolve.dll FlowNetworkStencils.dll SimuSolve.dll
	$(CSC) -r:PlotUtil.dll -r:FlowNetworkStencils.dll -r:SimuSolve.dll $(PNAME2).cs
	mono $(PNAME2).exe


PlotUtil.dll:$(PLOTUITL_DIR)/PlotUtil.cs
	$(CSC) /target:library $(PLOTUITL_DIR)/PlotUtil.cs $(MLIBS) 
	cp $(PLOTUITL_DIR)/PlotUtil.dll .

SimuSolve.dll:$(SIMUSOLVE_DIR)/SimuSolve.cs
	$(CSC) /target:library $(SIMUSOLVE_DIR)/SimuSolve.cs $(MLIBS) 
	cp $(SIMUSOLVE_DIR)/SimuSolve.dll .

FlowNetworkStencils.dll:FlowNetworkStencils.cs
	$(CSC) /target:library FlowNetworkStencils.cs



zip:
	zip wn.zip Makefile *.cs

# eof





