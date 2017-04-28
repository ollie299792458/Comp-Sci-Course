#
#  Chebychev basis illustration.
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2014.  
#

include ../Makefile.inc


all:
	$(SMLRUN) <cheby_eval.ml
	nohup	gnuplot --persist -e "set key bottom;plot \"plotdata\" using 1:2 title 'Cheby' with lines, \"plotdata\" using 1:3 title 'Taylor' with lines, \"plotdata\" using 1:4 title 'exp(x)' with lines"	

#
spare2:
	nohup	gnuplot --persist -e "set key bottom;plot \"plotdata\" using 1:2 title 'Cheby' with lines, \"plotdata\" using 1:3 title 'Taylor' with lines, \"plotdata\" using 1:4 title 'log(1+x)' with lines"	


spare:

	rm -f plotdata
	poly<cheby.ml 
	nohup	gnuplot --persist -e "plot \"plotdata\" using 1:2 title 'A' with lines, \"plotdata\" using 1:3 title 'B' with lines"	

# eof
