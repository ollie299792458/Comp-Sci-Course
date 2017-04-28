(*
# University of Cambridge Computer Laboratory - Numerical Methods Demos
# (C) DJ Greaves, 2015.
#
# Soyuz space craft FDTD simulation. - OCaml version.
*)

open Graphics
open Printf
open List


(* This is OCaml, not Standard ML - minor differences exist ... *)

type rocket_stage_t =
    {
        name:       string;
        net_weight: float;  (*// kilograms. Mass of unladen stage.*)
        fuel_payload:  float;  
        burn_time:  float;  (*// seconds to expend all fuel at maximum burn rate *)
        peak_thrust: float; (*// Newtons generated at maximum burn rate *)
    }


let stage_data =
  [|
      { name="boosters";     net_weight=3.8e3*. 4.0;  fuel_payload=44.5e3*. 4.0; burn_time=118.0; peak_thrust=813e3*. 4.0; }; (*// four of these.*)
      { name="second-stage"; net_weight=6.9e3;  fuel_payload=95.4e3; burn_time=290.0; peak_thrust=779e3; };
      { name="third-stage";  net_weight=21.4e3; fuel_payload=2.4e3;  burn_time=240.0; peak_thrust=298e3; };
      { name="payload";      net_weight=5e3;    fuel_payload=0.0;    burn_time=0.0;   peak_thrust=0.0; }; 
  |]

type state_vector =
    {
        stage_in_flight: int;    (*// Burn stage.             *)
        altitude: float;        (*// Height above ground (m) *)
        velocity: float;        (*// Vertical speed (m/s)    *)
        fuel_remaining: float;  (*// kg in current stage.    *)
    }

let time_step = 1.0
let throttle_setting = 0.98

let gravitational_force altitude mass =
    let g_at_surface = 9.81 in
    let radius_of_earth = 6371.0e3   in
    let f0 = mass *. g_at_surface  in
    let depreciate_gravity = true   in(*// Decrease the gravitational field with altitude ? *)
    if depreciate_gravity then
        let factor =  radius_of_earth /. (altitude +. radius_of_earth) in
        f0 *. factor *. factor
    else f0 
    
let int_range a b =
    let rec int_range_rec l a b =
    if a > b then l
    else int_range_rec (b :: l) a (b - 1)
  in (int_range_rec [] a b)

let current_mass sv =
    let tally_mass sofar stage_no =
        let stage = stage_data.(stage_no) in
        sofar +. stage.net_weight +. (if stage_no=sv.stage_in_flight then sv.fuel_remaining else stage.fuel_payload) in
    fold_left tally_mass 0.0 (int_range (sv.stage_in_flight) (* .. *) (Array.length stage_data - 1)) 

let current_thrust sv = 
    let stage = stage_data.(sv.stage_in_flight) in
    stage.peak_thrust *. throttle_setting

let fuel_rate sv =
    let stage = stage_data.(sv.stage_in_flight) in
    let pumping_rate = if stage.burn_time = 0.0 then 0.0 else stage.fuel_payload /. stage.burn_time in
    pumping_rate *. throttle_setting

let sim_time_step sv =
    let mass = current_mass sv in
    let force = current_thrust sv -. gravitational_force sv.altitude mass in
    let acceleration = force /. mass in
    let sv = { sv with fuel_remaining = max 0.0 (sv.fuel_remaining -. fuel_rate sv *. time_step); } in
    let sv =
        if (sv.altitude < 0.0) then {  sv with altitude = 0.0; velocity = max 0.0 sv.velocity;     }
        else
        { sv with velocity = sv.velocity +. acceleration *. time_step;
                  altitude = sv.altitude +. sv.velocity *. time_step;

        } in
    if sv.fuel_remaining > 0.0 || sv.stage_in_flight = Array.length stage_data - 1 then sv 
    else
        { sv with stage_in_flight = sv.stage_in_flight + 1;
                  fuel_remaining = stage_data.(sv.stage_in_flight + 1).fuel_payload;
        }

let initial_state_vector = { stage_in_flight=0; altitude=0.0; velocity=0.0; fuel_remaining=stage_data.(0).fuel_payload; }

;;

let minisleep (sec: float) =
    ignore (Unix.select [] [] [] sec)


let main() =
    let _ = print_string("FDTD Soyuz Simulation") in
    let (w, h) = (950, 550) in
    let _ = Graphics.open_graph (sprintf " %ix%i" w h) in
    let _ = Graphics.lineto 0 0 in

    let dplot time sv =
        let colour = if sv.stage_in_flight mod 2 = 0 then 0 else 128 in
        Graphics.lineto (int_of_float(time/. 1.5)) (10+int_of_float(sv.altitude /. 1200.0))
        in

    let rec simulate steps time sv =
        let _ = minisleep 0.02 in
        let sv = sim_time_step sv in
        let _ = print_string(sprintf "  Time=%1.2f Stage=%i Velocity=%f Altitude=%f " time sv.stage_in_flight sv.velocity sv.altitude) in
        let _ = print_string(sprintf "  Time=%1.2f f=%f \n" time sv.fuel_remaining) in
        let _ = dplot time sv in
        let _ = if sv.altitude < 0.0 then print_string(sprintf "Crash at %f m/s\n" sv.velocity) else () in
        if sv.altitude < 0.0 || steps <= 0 then sv else simulate (steps-1) (time +. time_step) sv
        in
    let _ = simulate 10000 0.0 initial_state_vector  in
    let _ = print_string "Press return to exit. " in
    let _ = read_line () in
    ()

;;

let _ = main()

(*
1st stage 4 boosters - 3.8 static + 44.5 fuel - 118secs 813 kN to 991 in vacuum
2nd stage 6.9 + 95.4 tones, 290s  779 kN 997 kN
3rd stage 21.4 + 2.4   240s   298 kN
*)


(* eof *)

