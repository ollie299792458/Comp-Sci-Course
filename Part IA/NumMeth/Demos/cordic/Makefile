#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (* An implementation and test of the CORDIC technique for sine and cosine *)
# (* (C) 2014 DJ Greaves, University of Cambridge.                          *)

#
#



#
#
#
include ../Makefile.inc

all: runC 
# runML runJava

PNAME=cordic

runC:
	gcc $(PNAME).c -lm
	./a.out

runML:
	$(SMLRUN) $(PNAME).sml

runJava:
	$(JAVAC) Cordic.java
	$(JAVARUN) Cordic



# eof
