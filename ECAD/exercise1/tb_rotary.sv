/*
 * Executes a couple of tests on the student-deviced Rotary Control
 * Based on a design by Vlad Gavrila
 */

`timescale 1ns/1ns

module tb_rotary
  (
	output logic 		clk,
	output logic 		rst,
	output logic [1:0] 	in,
	output logic [7:0] 	correct,
	output logic [7:0] 	count
   );

	logic cw, ccw;

	rotary dut (
		.clk(clk),
		.rst(rst),
		.rotary_in(in),
		.rotary_pos(count),
		.rot_cw(cw),
		.rot_ccw(ccw)
	);

	int numerr;
	bit endtest;

	// initialise clock and generate a reset pulse
	initial begin
		clk = 1;
		rst = 1;
		numerr = 0;
		endtest = 0;
		correct = 0;
		in = 2'b00;
		#20 rst = 0;

		$display("%010t ---------- Start simulation. Counter should be %d, is %d ----------", $time, correct, count);

		// Clean left turn
		#1000000 in = 2'b10;
		#1000000 in = 2'b11;
		#2000000 in = 2'b01;
		#1000000 in = 2'b00;
		#10000000 correct = correct - 1;
		$display("%010t ---------- Clean left turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Clean right turn
		#1000000 in = 2'b01;
		#1000000 in = 2'b11;
		#2000000 in = 2'b10;
		#1000000 in = 2'b00;
		#10000000 correct = correct + 1;
		$display("%010t ---------- Clean right turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Bouncy left turn
		#1000000 in = 2'b10;
		repeat(200) begin
			#100 in[1] = ~in[1];
		end
		#1000000 in = 2'b11;
		repeat(200) begin
			#100 in[0] = ~in[0];
		end
		#2000000 in = 2'b01;
		repeat(200) begin
			#100 in[1] = ~in[1];
		end
		#1000000 in = 2'b00;
		repeat(200) begin
			#100 in[0] = ~in[0];
		end
		#10000000 correct = correct - 1;
		$display("%010t ---------- Bouncy left turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		repeat(5) begin
			//Bouncy right turn
			#1000000 in = 2'b01;
			repeat(200) begin
				#100 in[0] = ~in[0];
			end
			#1000000 in = 2'b11;
			repeat(200) begin
				#100 in[1] = ~in[1];
			end
			#2000000 in = 2'b10;
			repeat(200) begin
				#100 in[0] = ~in[0];
			end
			#1000000 in = 2'b00;
			repeat(200) begin
				#100 in[1] = ~in[1];
			end
			#10000000 correct = correct + 1;
			$display("%010t ---------- Bouncy right turn. Counter should be %d, is %d ----------", $time, correct, count);
			if (correct != count) numerr = numerr + 1;
		end

		// Long bouncy left turn
		#1000000 in = 2'b10;
		repeat(20) begin
			#1000 in[1] = ~in[1];
		end
		#1000000 in = 2'b11;
		repeat(20) begin
			#1000 in[0] = ~in[0];
		end
		#2000000 in = 2'b01;
		repeat(20) begin
			#1000 in[1] = ~in[1];
		end
		#1000000 in = 2'b00;
		repeat(20) begin
			#1000 in[0] = ~in[0];
		end
		#10000000 correct = correct - 1;
		$display("%010t ---------- Long bouncy left turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Long bouncy right turn
		#1000000 in = 2'b01;
		repeat(20) begin
			#1000 in[0] = ~in[0];
		end
		#1000000 in = 2'b11;
		repeat(20) begin
			#1000 in[1] = ~in[1];
		end
		#2000000 in = 2'b10;
		repeat(20) begin
			#1000 in[0] = ~in[0];
		end
		#1000000 in = 2'b00;
		repeat(20) begin
			#1000 in[1] = ~in[1];
		end
		#10000000 correct = correct + 1;
		$display("%010t ---------- Long bouncy right turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Very long bouncy left turn
		#1000000 in = 2'b10;
		repeat(20) begin
			#1000 in[1] = ~in[1];
		end
		#1000000 in = 2'b11;
		repeat(20) begin
			#1000 in[0] = ~in[0];
		end
		#2000000 in = 2'b01;
		repeat(20) begin
			#1000 in[1] = ~in[1];
		end
		#1000000 in = 2'b00;
		repeat(20) begin
			#1000 in[0] = ~in[0];
		end
		#10000000 correct = correct - 1;
		$display("%010t ---------- Very long bouncy left turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Very long bouncy right turn
		#1000000 in = 2'b01;
		repeat(2) begin
			#100000 in[0] = ~in[0];
		end
		#1000000 in = 2'b11;
		repeat(2) begin
			#100000 in[1] = ~in[1];
		end
		#2000000 in = 2'b10;
		repeat(2) begin
			#100000 in[0] = ~in[0];
		end
		#1000000 in = 2'b00;
		repeat(2) begin
			#100000 in[1] = ~in[1];
		end
		#10000000 correct = correct + 1;
		$display("%010t ---------- Very long bouncy right turn. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Wild left bounce
		repeat(2) begin
			#100000 in[0] = ~in[0];
		end
		$display("%010t ---------- Wild left bounce. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;

		// Wild right bounce
		repeat(2) begin
			#100000 in[1] = ~in[1];
		end
		$display("%010t ---------- Wild right bounce. Counter should be %d, is %d ----------", $time, correct, count);
		if (correct != count) numerr = numerr + 1;
		endtest = 1;
	end

	// oscilate the clock
	always #5 clk = !clk;
	always @ (numerr) $display(" - ERROR");
	always @ (endtest) begin
		if (numerr == 0) $display("SUCCESS");
		else $display("FAILED with %d errors", numerr);
		$finish();
	end
endmodule
