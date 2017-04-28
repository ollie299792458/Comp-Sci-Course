(* University of Cambridge Computer Laboratory - Numerical Methods Demos
 * (C) 2014 DJ Greaves, University of Cambridge.                          
 * Verhulst's logistic map
 *)

(* see the following plot: gnuplot> plot [-1:2] x, 4*x*(1-x)   *)

(* Verhulst's logistic map : preduces chaos: an iteration that just jumps around a lot.*)

fun verhulst x = 4.0 * x * (1.0 - x)  
;


fun it n x = if n = 0 then [] else let val x' = verhulst x in x :: it (n-1) x' end
;

it 33 0.2

(* eof *)
