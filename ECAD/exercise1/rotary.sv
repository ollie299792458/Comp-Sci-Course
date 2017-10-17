// rotary decoder template

module rotary
  (
    input  wire clk,
    input  wire rst,
    input  wire [1:0] rotary_in,
    output logic [7:0] rotary_pos,
    output logic rot_cw,
    output logic rot_ccw
   );

/* Wire and register definitions */
logic [7:0] last_pos;
logic [1:0] clean_rotary;

/* Debouncing components */
debounce(.clk(clk), .rst(rst), .bouncy_in(rotary_in[0]), .clean_out(clean_rotary[0]));
debounce(.clk(clk), .rst(rst), .bouncy_in(rotary_in[1]), .clean_out(clean_rotary[1]));

/* Outout manipulation logic */
always_ff @(posedge clk or posedge rst)
if (rst)
begin
    clk <= 0;
    clean_rotary <= 0;
    last_pos <= 0;
end 
else
begin
    if (last_pos != clean_rotary) begin
        if (clean_rotary == 2'b00) begin
            if (last_post == 2'b01) begin
                rot_cw <= 1;
            end else if (last_post == 2'b10) begin
                rot_ccw <= 1;
            end;
        end;
        if (last_pos == 2'b00) begin
            rot_cw <= 0;
            rot_ccw <= 0;
        end;
    end;
    last_pos <= clean_rotary;
end;

endmodule // rotarydecoder
