#
#  Loss of significance demo.
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.
#



#
#
#
include ../Makefile.inc

PNAME=loss_sig


all: runML runC runJava

runML:
	$(SMLRUN) < $(PNAME).ml


runC:
	gcc $(PNAME).c
	./a.out

runJava:
	$(JAVAC) LossSig.java
	$(JAVARUN) LossSig




# eof
