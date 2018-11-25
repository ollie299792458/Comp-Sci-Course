(* fpout.ml
 * ML Version of the floating point to ASCII conversion function.
 * (C) 2014 DJ Greaves, University of Cambridge, Computer Laboratory.
 *)

(* Fixed point integer output 0 as needed for the exponent rendering *)
   val tens_table = Array.fromList [1,10,100,1000,10000,100000,1000000,10000000, 100000000];

   fun bin2ascii d0 =
       let fun b2a d0 =
	   let fun scanup p = if Array.sub(tens_table, p) > d0 then p-1 else scanup(p+1)
	       val p0 = scanup 0
	       fun digits d0 p =
		   if p<0 then [] else
		   let val d = d0 div Array.sub(tens_table, p)
		       val r = d0 - d * Array.sub(tens_table, p)
		   in chr(48 + d) :: digits r (p-1) end
               in digits d0 p0 end
       in if (d0 < 0) then #"~" :: b2a(0-d0) else b2a d0 
       end  


  (* These tables need to be a bit bigger in reality, to handle the whole range of 16sf and E308 found in the IEEE standard... *)

  val f_tens_table = Vector.fromList [1E0,1E1,1E2,1E3,1E4,1E5,1E6,1E7,1E8];  

  val bin_exps_table =   [ (1.0E32, 32),  (1.0E16, 16),  (1.0E8, 8),  (1.0E4, 4),  (1.0E2, 2),  (1.0E1, 1) ];  
  val bin_fracs_table =  [ (1.0E~32, 32), (1.0E~16, 16), (1.0E~8, 8), (1.0E~4, 4), (1.0E~2, 2), (1.0E~1, 1) ];  



fun float_to_string precision d00 = (*Entry point*)
  let val lower_bound = Vector.sub(f_tens_table, precision)
      val upper_bound = Vector.sub(f_tens_table, precision+1)
      val (d0, sign) = if d00 < 0.0 then (0.0-d00, [#"-"]) else (d00, [])
      fun chop_upwards ((ratio, bitpos), (d0, exponent)) = 
           let val q = d0 * ratio
           in if q < upper_bound then  (q, exponent - bitpos) else (d0, exponent)
           end

      fun chop_downwards ((ratio, bitpos), (d0, exponent)) = 
           let val q = d0 * ratio
           in if q > lower_bound then  (q, exponent + bitpos) else (d0, exponent)
           end

      val (d0, exponent) = if d0 < lower_bound then foldl chop_upwards (d0, 0) bin_exps_table
                                               else foldl chop_downwards (d0, 0) bin_fracs_table

      val imant = floor d0 (* Convert mantissa to integer. *)

      val exponent = exponent + precision

      (* Decimal point will only move a certain distance: outside that range force scientific form. *)
      val scientific_form = exponent > precision orelse exponent < 0

      fun digits d0 p trailzero_supress =
           if p<0 orelse (trailzero_supress andalso d0=0) then [] else
           let val d = d0 div Array.sub(tens_table, p)
               val r = d0 - d * Array.sub(tens_table, p)
               val dot_time =  (p = precision + (if scientific_form then 0 else 0-exponent))
               val rest = digits r (p-1) (trailzero_supress orelse dot_time)
               val rest = if dot_time then #"."::rest else rest (* render decimal point *)
               in if d>9 then #"?" :: bin2ascii d0 @ #"!" :: rest else chr(ord(#"0") + d) :: rest end


      val mantissa = digits imant precision false
      val exponent = if scientific_form then #"e" :: bin2ascii exponent else []
      in (d00, imant, implode(sign @ mantissa @ exponent) )
      end
;

(* Some test data ...*)
map (float_to_string 4) [ 1.0, 10.0, 1.23456789, ~2.3E19, 4.5E~19 ];

(* > val it =
 * [(1.0, 10000, "1."),
 * (10.0, 10000, "10."),
 * (1.23456789, 12345, "1.2345"), 
 * (~2.3E19, 23000, "-2.3e19"),
 * (4.5E~19, 45000, "4.5e~19")]
 *)

(* eof *)
