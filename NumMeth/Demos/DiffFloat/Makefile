#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# 
# Numerical differentiation.
#
# (C) DJ Greaves, 2015.  Alan Mycroft, Siliva Breu, Cathryn Gray.

#
#



#
#
#
include ../Makefile.inc

all: runC runJava

PNAME=diff_float

runC:
	gcc $(PNAME).c -lm
	./a.out

runML:
	$(SMLRUN) < $(PNAME).sml

runJava:
	$(JAVAC) DiffFloat.java
	$(JAVARUN) DiffFloat



# eof
