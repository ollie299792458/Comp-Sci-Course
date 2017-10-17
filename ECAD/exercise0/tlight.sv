module tlight(input logic clk,
              output logic r,
              output logic a,
              output logic g);

logic [2:0] state;

// enter code here
always_ff @(posedge clk)
initial state=0;
begin
  state = (state == 3'b100) ? 3'b110:
          (state == 3'b110) ? 3'b001:
          (state == 3'b001) ? 3'b010:
          (state == 3'b010) ? 3'b100:
          3'b001;
end;

assign g = state[0];
assign a = state[1];
assign r = state[2];
endmodule // tlights
