//
// University of Cambridge Computer Laboratory - Numerical Methods Demos
// (C) DJ Greaves, 2015.
//

//
// Soyuz Rocket Simulation
//

open PlotUtil

let println (s:string) = System.Console.WriteLine s

type rocket_stage_t =
    {
        name:       string;
        net_weight: double;  // kilograms. Mass of unladen stage.
        fuel_payload:  double;  //
        burn_time:  double;  // seconds to expend all fuel at maximum burn rate
        peak_thrust: double; // Newtons generated at maximum burn rate
    }


let stage_data =
  [
      { name="boosters";     net_weight=3.8e3*4.0;  fuel_payload=44.5e3*4.0; burn_time=118.0; peak_thrust=813e3*4.0; }; // four of these.
      { name="second-stage"; net_weight=6.9e3;  fuel_payload=95.4e3; burn_time=290.0; peak_thrust=779e3; };
      { name="third-stage";  net_weight=21.4e3; fuel_payload=2.4e3;  burn_time=240.0; peak_thrust=298e3; };
      { name="payload";      net_weight=5e3;    fuel_payload=0.0;    burn_time=0.0;   peak_thrust=0.0; };            
  ]

type state_vector =
    {
        stage_in_flight: int;    // Burn stage.
        altitude: double;        // Height above ground (m)
        velocity: double;        // Vertical speed (m/s)
        fuel_remaining: double;  // kg in current stage.
    }

let time_step = 1.0
let throttle_setting = 0.98

let gravitational_force altitude mass =
    let g_at_surface = 9.81
    let radius_of_earth = 6371e3 
    let f0 = mass * g_at_surface 
    let depreciate_gravity = true  // Decrease the gravitational field with altitude ?
    if depreciate_gravity then
        let factor =  radius_of_earth / (altitude+radius_of_earth)
        f0 * factor * factor
    else f0
    
let current_mass sv =
    let tally_mass sofar stage_no =
        let stage = stage_data.[stage_no]
        sofar + stage.net_weight + (if stage_no=sv.stage_in_flight then sv.fuel_remaining else stage.fuel_payload)
    List.fold tally_mass 0.0 [sv.stage_in_flight .. stage_data.Length - 1] 



let current_thrust sv = 
    let stage = stage_data.[sv.stage_in_flight]
    stage.peak_thrust * throttle_setting

let fuel_rate sv =
    let stage = stage_data.[sv.stage_in_flight]
    let pumping_rate = stage.fuel_payload / stage.burn_time 
    pumping_rate * throttle_setting

    

let sim_time_step sv =
    let mass = current_mass sv
    let force = current_thrust sv - gravitational_force sv.altitude mass
    
    let acceleration = force / mass
    let sv = { sv with fuel_remaining = max 0.0 (sv.fuel_remaining - fuel_rate sv * time_step); }
    let sv =
        if (sv.altitude < 0.0) then {  sv with altitude = 0.0; velocity = max 0.0 sv.velocity;     }
        else
        { sv with velocity = sv.velocity + acceleration * time_step;
                  altitude = sv.altitude + sv.velocity * time_step;

        }
    if sv.fuel_remaining > 0.0 || sv.stage_in_flight = stage_data.Length-1 then sv
    else
        { sv with stage_in_flight = sv.stage_in_flight + 1;
                  fuel_remaining = stage_data.[sv.stage_in_flight + 1].fuel_payload;
        }

let initial_state_vector = { stage_in_flight=0; altitude=0.0; velocity=0.0; fuel_remaining=stage_data.[0].fuel_payload; }

let main() =


    let _ = println("FDTD Soyuz Simulation")
    let p = new Plotter()
    let (w, h) =  (1200u, 500u) // Plot size
    let _ = p.Open(w, h)


    let plot time sv =
        let colour = if sv.stage_in_flight % 2 = 0 then 0 else 128
        p.Setget_pixel(uint32(time /1.0), h-40u-uint32(sv.altitude/1200.0), true, colour);
        ()
        
    let rec simulate steps time sv =
        let sv = sim_time_step sv

        plot time sv
        println(sprintf "  Time=%1.2f Stage=%i Velocity=%f Altitude=%f " time sv.stage_in_flight sv.velocity sv.altitude)
        println(sprintf "  Time=%1.2f f=%f " time sv.fuel_remaining)
        if sv.altitude < 0.0 then println(sprintf "Crash at %f m/s" sv.velocity)
        if sv.altitude < 0.0 || steps <= 0 then sv else simulate (steps-1) (time+time_step) sv


    let _ = simulate 10000 0.0 initial_state_vector

    let rec spin() = spin() // Spin forever...
    let _ = spin()
    ()



let _ = main()

(*
1st stage 4 boosters - 3.8 static + 44.5 fuel - 118secs 813 kN to 991 in vacuum
2nd stage 6.9 + 95.4 tones, 290s  779 kN 997 kN
3rd stage 21.4 + 2.4   240s   298 kN
*)

// eof
