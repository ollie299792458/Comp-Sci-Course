(* University of Cambridge Computer Laboratory - Numerical Methods Demos *)
(* (C) 2012 DJ Greaves - Long Division *)

fun divide N D =
    let fun prescale p D = if N>D then prescale (p*2) (D*2) else (p, D)
        val (p, D) = prescale 1 D (* left shift loop *)

        fun mainloop N D p q = 
            if p=0 then q
            else 
                (* Binary decision - it either goes or doesn’t *)
                let val (N, q) = if N >= D then (N-D, q+p) else (N, q)
                     in mainloop N (D div 2) (p div 2) q 
                     end
       in mainloop N D p 0
       end
;

divide 1000000 81
;

divide 100000000 81
;


(* eof *)


