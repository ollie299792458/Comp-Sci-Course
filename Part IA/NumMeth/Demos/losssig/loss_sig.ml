(*
//#  Loss of significance demo.
//#
//# University of Cambridge Computer Laboratory - Numerical Methods Demos
//# (C) DJ Greaves, 2015.  Alan Mycroft, Silvia Breu.
*)


val x_magic = 10.0/9.0
;

val x = x_magic
;

fun iterated_func x = (x-1.0) * 10.0
;

fun iterate n x = if n <= 0 then []
    else
        let val x' = iterated_func x
            val _ = print (Real.toString x' ^ "\n")
        in x :: (iterate (n-1) x')
        end     
;
val ans = iterate 30 x_magic
;

(* eof *)


