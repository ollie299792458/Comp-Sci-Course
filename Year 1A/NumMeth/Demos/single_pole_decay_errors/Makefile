#
#  single_pole exponential decay
#
# Exploration of how errors accumulate in this one particularl, but not uncommon, case.
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#

# With a forward stencil the starting point for the next step has suffered from over decay and so that step decays less.
# Ultimately, in this instance, this effect cancels out (essentially owing to negative feedback) as a slightly shifted
# exponential will precisely fit the resulting geometric series. Error is finite and bounded.

#
#
#
include ../Makefile.inc

PNAME=single_pole_decay

all:
	$(SMLRUN) < $(PNAME).ml

