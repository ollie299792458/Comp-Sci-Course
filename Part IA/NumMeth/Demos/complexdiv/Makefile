#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (* (C) 2015 DJ Greaves, University of Cambridge.                          *)
# Due to Nick Maclaren

# Complex division



#
#
#
include ../Makefile.inc

all: runC runJava
# runML 

PNAME=complexdiv

runC:
	gcc $(PNAME).c -lm
	./a.out

runML:
	$(SMLRUN) $(PNAME).sml

runJava:
	$(JAVAC) ComplexDiv.java
	$(JAVARUN) ComplexDiv




# eof
