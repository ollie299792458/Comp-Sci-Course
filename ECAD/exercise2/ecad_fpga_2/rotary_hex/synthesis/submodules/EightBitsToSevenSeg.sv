// example Qsys module
// Qsys insists we take a clock and reset, while we don't need them here

module EightBitsToSevenSeg(
	input clock,
	input reset,
	input [7:0] hexval,
	output [6:0] digit0,
	output [6:0] digit1
);
        function [6:0] ledcode;
                input [3:0] hexval;

                case (hexval)
                        4'h0: ledcode = 7'b0111111;
                        4'h1: ledcode = 7'b0000110;
                        4'h2: ledcode = 7'b1011011;
                        4'h3: ledcode = 7'b1001111;
                        4'h4: ledcode = 7'b1100110;
                        4'h5: ledcode = 7'b1101101;
                        4'h6: ledcode = 7'b1111101;
                        4'h7: ledcode = 7'b0000111;
                        4'h8: ledcode = 7'b1111111;
                        4'h9: ledcode = 7'b1101111;
                        4'ha: ledcode = 7'b1110111;
                        4'hb: ledcode = 7'b1111100;
                        4'hc: ledcode = 7'b0111001;
                        4'hd: ledcode = 7'b1011110;
                        4'he: ledcode = 7'b1111001;
                        4'hf: ledcode = 7'b1110001;
                endcase
        endfunction


	always_comb begin
		digit0 = ~ledcode(hexval[3:0]);
		digit1 = ~ledcode(hexval[7:4]);
	end
endmodule
