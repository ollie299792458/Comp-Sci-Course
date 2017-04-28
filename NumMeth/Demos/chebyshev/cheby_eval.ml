(*
#  Chebychev basis illustration.
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2014.  
*)


fun range2 x ev (delta:real) =
    if x>ev then [] else x :: (range2 (x+delta) ev delta)
;


(* ML coding of Tchebychev basis functions *)
fun compute_cheby n x =
    let fun tch 0 = (1.0, 123456.0) (*snd arbitrary*)
        |   tch 1 = (x, 1.0)
        |   tch n = 
                let val (p, pp) = tch (n-1)
                in (2.0 * x * p - pp, p) end
    val (ans, _) = tch n 
    in ans end
;


(* crude mid-point rule numeric integration to get nth Chebby coef for expansion of f. *)
(* Simpson's Rule would be nicer - or better still do it in Octave/Matlab !            *)
fun cheby_coef n f = 
    let val h = 0.00001 (* arbitrary choice of h !  Oh dear! *)
        val points = (range2 (~1.0+h) (1.0-h) h)
        val sum = foldl (fn (x,c) => f x  * compute_cheby n x / Math.sqrt(1.0 -x*x) + c) 0.0 points
        val ans = sum * h  * 2.0 / Math.pi
    in if n=0 then ans/2.0 else ans
    end


(* val coefs = map (fn x=>(x, cheby_coef x (fn r=>Math.ln(1.0+r))))  [0,1,2,3]  *)

val coefs = map (fn x=>(x, cheby_coef x (fn r=>Math.exp(r))))  [0,1,2]


fun eval x = foldl (fn ((n,a),c) =>  c + a * compute_cheby n x) 0.0 coefs
;


(* log(1+x) = x - 1/2 x^2 + 1/3 x^3 ... 
fun Taylor_log_1_plus_x n x =
   let fun sum c p q = if p>=n then c else sum (c + q/real(p)) (p+1) (0.0-q*x)
   in sum 0.0 1 x  
   end

val toPlot = map (fn x => (x, eval x, Taylor_log_1_plus_x 2 x, Math.ln(1.0+x))) (range2 ~1.0 1.0 0.02)
*)

(* exp(x) = 1 + x + x^2/2! + x^3/3! ... *)
fun Taylor_exp_x n x =
   let fun sum c p nn dd = if p>n then c else sum (c + nn/dd) (p+1) (nn*x) (dd*real(p+1))
   in sum 1.0 1 x 1.0 
   end


(* Of course, once the Chebyshev coefficients have been generated it would be normal practice
 * to now collate on powers of x to get a normal polynomial.  It is then interesting to compare
 * these values with the ones given by Taylor.
 *)

val toPlot = map (fn x => (x, eval x, Taylor_exp_x 2 x, Math.exp(x))) (range2 ~1.0 1.0 0.02)


val outstream = TextIO.openOut("plotdata")
;

fun r2a x = 
   let fun p1 x = if (x > 0.0001) then Real.toString x else "0.0"
   in if x < 0.0 then "-" ^ p1 (0.0-x) else p1 x end
;

app (fn (x,y1,y2,y3) => TextIO.output(outstream, r2a x ^ "  " ^ r2a y1 ^ " " ^ r2a y2 ^ " " ^ r2a y3 ^ "\n")) toPlot 
;


(* > log(1+x)
 val coefs =
   [(0, ~0.6796021815), (1, 1.96805474), (2, ~0.972910111),
    (3, 0.6347218153), (4, ~0.4729104385)]: (int * real) list

  >exp(x)
 val coefs = [(0, 1.260923288), (1, 1.121562611), (2, 0.2612103519)]:  (int * real) list

You can plot the output with gnuplot using something like:
	gnuplot --persist -e "set key bottom;plot \"plotdata\" using 1:2 title 'Cheby' with lines, \"plotdata\" using 1:3 title 'Taylor' with lines, \"plotdata\" using 1:4 title 'exp(x)' with lines"	
*)



(* eof *)
