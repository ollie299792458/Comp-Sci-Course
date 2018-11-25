module alternating_counters(
  input clock,
  input reset,
  input latch,
  output logic out_a,
  output logic out_b
);

  logic [7:0] count_a;
  logic [7:0] count_b;

  initial begin
    count_a <= 8'h0;
    count_b <= 8'h80;
  end
     
  always @(clock) begin

    count_a <= count_a - 1;
    count_b <= count_b + 1;

    out_b = latch ? count_b : out_a;
    out_a = latch ? count_a : out_b;  

  end
   
endmodule
