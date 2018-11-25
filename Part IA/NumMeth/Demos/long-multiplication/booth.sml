(* University of Cambridge Computer Laboratory - Numerical Methods Demos *)
(* (C) 2012 DJ Greaves - Booth Radix 4 *)



exception Failed of int * int
;

fun booth(x, y, c, carry) =
    if(x=0 andalso carry=0) then c else
    let val x' = x div 4
	val y' = y * 4
	val n  = (x mod 4) + carry
	val (carry', c') = case (n) of
		 (0) => (0, c)
		|(1) => (0, c+y)
		|(2) => (0, c+2*y)
		|(3) => (1, c-y)
		|(4) => (1, c)

	in booth(x', y', c', carry')
    end
;


(* Booth's multiplier is twice as fast as binary long multiplication
but still only uses a single adder.  The trick is that the adder is
sometimes used as a subtractor, using the identity 3=4-1.  The
multiplication and division and modulus by powers of 2 are all
performed with wiring and so require no gates to perform. (The
fixed-width addition of the carry to form n is considered part of the
control logic, rather than counting as a datapath adder.)  *)


fun tester(x, y) = 
	if booth(x, y, 0, 0) <> x*y then raise Failed(x,y) else ()

;

fun run1(x, y) = if (x < 3000) then (tester(x, y); run1(x+1, y)) else ()
;

fun run2(y) = if (y < 30) then (run1(0, y); run2(y+1)) else ()
;

run2(0);

(* eof *)

