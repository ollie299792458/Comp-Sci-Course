(*
#  single_pole exponential decay
#
# Exploration of how errors accumulate in this one particularl, but not uncommon, case.
#
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#

# With a forward stencil the starting point for the next step has suffered from over decay and so that step decays less.
# Ultimately, in this instance, this effect cancels out (essentially owing to negative feedback) as a slightly shifted
# exponential will precisely fit the resulting geometric series. Error is finite and bounded.


*)


fun drop 0 lst  = lst
|   drop n (h::t) = drop (n-1) t
;



val alpha = 0.01;
val deltaT = 1.0;

fun run steps (time, fdtd_value) =
    let val ideal = Math.exp(0.0-time * alpha)
    val error = fdtd_value - ideal
    val fdtd_value' = fdtd_value - fdtd_value * alpha * deltaT
    val time' = time + deltaT
in
   if steps <0 then [] else (time, ideal, fdtd_value, error) :: run (steps-1) (time', fdtd_value')
   end					;



drop 30 (run (100) (0.0, 1.0))

(*
val it =
   [(0.0, 1.0, 1.0, 0.0), (1.0, 0.9900498337, 0.99, ~0.00004983374917),
    (2.0, 0.9801986733, 0.9801, ~0.00009867330676),
    (3.0, 0.9704455335, 0.970299, ~0.0001465335485),
    (4.0, 0.9607894392, 0.96059601, ~0.0001934291523),
    (5.0, 0.9512294245, 0.9509900499, ~0.0002393746007),
    (6.0, 0.9417645336, 0.9414801494, ~0.0002843841832),
    (7.0, 0.9323938199, 0.9320653479, ~0.000328471999),
    (8.0, 0.9231163464, 0.9227446944, ~0.0003716519587),
    
...
    (10.0, 0.904837418, 0.904382075,   ~0.0004553430272),
    (11.0, 0.8958341353, 0.8953382543, ~0.0004958810378),
    (12.0, 0.8869204367, 0.8863848717, ~0.000535565001),
    (13.0, 0.8780954309, 0.877521023,  ~0.0005744079216),
    (14.0, 0.8693582354, 0.8687458128, ~0.0006124226298),
    (15.0, 0.8607079764, 0.8600583546, ~0.0006496217838),
    (16.0, 0.852143789, 0.8514577711,  ~0.0006860178713),
    (17.0, 0.8436648166, 0.8429431934, ~0.0007216232125),
    (18.0, 0.8352702114, 0.8345137615, ~0.0007564499612),
...
    (30.0, 0.7408182207, 0.7397003734, ~0.001117847293),
    (31.0, 0.7334469562, 0.7323033697, ~0.00114358657),
    (32.0, 0.7261490371, 0.724980336,  ~0.001168701116),
    (33.0, 0.7189237334, 0.7177305326, ~0.001193200834),
    (34.0, 0.7117703228, 0.7105532273, ~0.00121709549),
    (35.0, 0.7046880897, 0.703447695,  ~0.001240394719),
    (36.0, 0.6976763261, 0.696413218,  ~0.001263108021),
    (37.0, 0.6907343306, 0.6894490859, ~0.001285244768),
    (38.0, 0.6838614092, 0.682554595,  ~0.001306814202),
    (39.0, 0.6770568745, ...), ...]: (real * real * real * real) list

 *)

(* eof *)


