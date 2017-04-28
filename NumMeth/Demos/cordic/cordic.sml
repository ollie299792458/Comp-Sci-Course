

type vector_2 = { re:real, im:real }
;

type matrix_2x2 = {a:real, b:real, c:real, d:real }
;



fun mpx4b2 (l:matrix_2x2) (r:vector_2)
   = { re= #a l * #re r + #b l * #im r,
       im= #c l * #re r + #d l * #im r }:vector_2
;


val identity2x2 = { a=1.0, b=0.0, c=0.0, d=1.0}
;

(*Angles in radians whose tangent is the reciprocal of a power of 2 *)
val Angles = Vector.fromList [ 0.78539816339745,   0.46364760900081,   0.24497866312686,   0.12435499454676,  0.06241880999596,   0.03123983343027,   0.01562372862048]
;

(* Correction factors *)
val Kvalues = Vector.fromList [ 0.70710678118655,  0.63245553203368,  0.61357199107790,  0.60883391251775,  0.60764825625617,   0.60735177014130,   0.60727764409353]
;

val log: (real * real) list ref = ref []
;

fun todegs rads = rads / Math.pi * 180.0; 

fun cordic loops arg =
  let fun iterate n p arg v = 
      if n=loops then v
      else
      let
         val (d,sigma) = if arg > 0.0 then (1.0, p) else (~1.0, ~p)
         val rotor = { a= 1.0, b= ~sigma, c= sigma, d= 1.0 } 
         (* val _ = log := (todegs arg, todegs(d * Vector.sub(Angles, n))) :: !log *)
      in iterate (n+1) (p/2.0) (arg - d * Vector.sub(Angles, n)) (mpx4b2 rotor v) end

  val rotated = iterate 0 1.0 arg { re=1.0, im=0.0 } 
  in #re rotated * Vector.sub(Kvalues, loops-1) 
  end
;

map (fn x=>(cordic 6 x, Math.cos x)) [ 0.0, Math.pi/4.0, 0.1,  ~ Math.pi/4.0, 1.0 ]
;

map (fn x=>(cordic 3 x, Math.cos x)) [ Math.pi/8.0]
;

(*   [(0.9998654553, 1.0), (0.707570002, 0.7071067812),
      (0.9942308442, 0.9950041653), (0.707570002, 0.7071067812),
      (0.5625158347, 0.5403023059)] : (real * Real.Math.real) list
*)

fun find_maxarg a = if a < 1.0E~40 then 0.0 else Math.atan(a) + find_maxarg(a/2.0)
;

val maxarg = todegs(find_maxarg 1.0);
;
val asn = rev(!log)
;

(* 
map (fn (x:vector_2)=> Math.sqrt(1.0/(#re x * #re x + #im x * #im x))) (!log)
; *)


(* fun mpx2b2 (l:matrix_2x2, r:matrix_2x2) = { a=l.a*r.a + l.b*r.c, b=l.a*r.c+l.b*r.d, c=l.c*r.a + l.d*r.c, d=l.c*r.c+l.d*r.d  }
;
fun mpx4b4 (l:matrix_2x2) (r:matrix_2x2)
   = { a= #a l * #a r + #b l * #c r, b= #a l * #c r + #b l * #d r, 
       c= #c l * #a r + #d l * #c r, d= #c l * #c r + #d l * #d r   }:matrix_2x2
;
*)
