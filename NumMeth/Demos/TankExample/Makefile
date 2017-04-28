#
# 
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2014-15.  
#
# Tank or Capacitor example - both vary in stored quantity according to the integral of the in and out flows.
# CapacitorExample2 - Time-varying simulation in circuit flow (Spice) style.

#
#
#
include ../Makefile.inc

SIMUSOLVE_DIR=../lu-decomposition
CLASSPATH=  -classpath .:$(SIMUSOLVE_DIR)

# Although this is numbered two, there does not seem to be a number one, so perhaps a historical anomaly.

PNAME=TankExample2

run:
	javac $(CLASSPATH) $(PNAME).java
	java $(CLASSPATH) $(PNAME)


gnuplot:
	# gnuplot runes:
	## System.out.printf("%f   %f %f  \n", sim_time, v[0], v[1]);
	#  set key right bottom
	# plot [0:0.004]"cap-plot" using 1:2 with lines title "v1", "cap-plot" using 1:3 with lines title "v2"


# eof
