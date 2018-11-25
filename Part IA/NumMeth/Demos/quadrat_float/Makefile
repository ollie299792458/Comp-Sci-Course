#
#  Quadratic equation solving - using an IF statement to avoid cancellation and loss of significance.
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.
#



#
#
#
include ../Makefile.inc

PNAME=quadrat_float


all: runC runJava

runML:
	$(SMLRUN) < $(PNAME).ml


runC:
	gcc $(PNAME).c -lm
	./a.out

runJava:
	$(JAVAC) QuadratFloat.java
	$(JAVARUN) QuadratFloat





# eof
