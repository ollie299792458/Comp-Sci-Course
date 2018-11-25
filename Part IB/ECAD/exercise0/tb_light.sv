`timescale 1ns / 1ps

module tb_tlight(
  output r,
  output a,
  output g);

  logic clk;      // clock signal we are going to generate

                  // instantiate design under test (dut)
  tlight dut(.clk(clk), .r(r), .a(a), .g(g));

  initial         // sequence of events to simulate
    begin
      clk = 0;   // at time=0 set clock to zero
    end

  always #5       // every five simulation units...
    clk <= !clk;  // ...invert the clock

                  // produce debug output on the negative edge of the clock
  always @(negedge clk)
    $display("time=%05d: (r,a,g) = (%1d,%1d,%1d)",
      $time,      // simulator time
      r, a, g);   // outputs to display: red, amber, green

endmodule // tb_tlight