(* University of Cambridge Computer Laboratory - Numerical Methods Demos *)
(* (C) 2012 DJ Greaves - Long Division - Fixed Latency. *)

fun printhex x =  TextIO.print (" 0x" ^ Int.fmt StringCvt.HEX x);
fun println _ =  TextIO.print ("\n");

val NUMBASE = 1073741824 (* Two to the power of 30. *)
;

(* We use the pair Nh,Nl to form a double-width register. *)


fun divide N D =
     let
        fun divloop (Nh, Nl) p q = 
         (  printhex Nh;    printhex Nl;   printhex p;    printhex q; println (); 
            if p=0 then q
            else 
                let val (Nh, Nl) = (Nh*2 + Nl div NUMBASE, (Nl mod NUMBASE)*2) in 
                let val (N, q) = if Nh >= D then ((Nh-D, Nl), q+p) else ((Nh, Nl), q)
                     in divloop N (p div 2) q 
                     end end
       ) in divloop (0, N) NUMBASE 0
    end
;

divide 1000000 81
;

divide 100000000 81
;

divide 256 0
;

(* eof *)


