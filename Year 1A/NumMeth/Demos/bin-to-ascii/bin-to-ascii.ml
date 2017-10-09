(* Integer Output - binary to ASCII characters.
//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
*)

val tens_table = Array.fromList [1,10,100,1000,10000,100000];

fun bin2ascii d0 =
       let fun scanup p = if Array.sub(tens_table, p) > d0 then p-1 else scanup(p+1)
       val p0 = scanup 0
       fun digits d0 p =
               if p<0 then [] else
               let val d = d0 div Array.sub(tens_table, p)
                   val r = d0 - d * Array.sub(tens_table, p)
               in chr(48 + d) :: digits r (p-1) end
       in digits d0 p0 end
;

bin2ascii 12345
;

(* val it = [#"1", #"2", #"3", #"4"]: char list *)


(* eof *)
