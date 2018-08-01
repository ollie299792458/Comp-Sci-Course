/*
 * Simple test of a debouncer
 * By Colin Rothwell
 */

`timescale 1ns/1ns

module tb_debounce ();

// Each test starts running at 10ms intervals.
// Clock is 50 MHz, so a period of 20 ns. So, we need to invert clock every
// 10 time units.
// "Settle time" is 1ms = 50,000 cycles
// "Blip time" is 0.1ms = 5,000 cycles
// Tests are 5ms, so 250,000 cycles

logic clk;
logic rst;
logic test_in;
logic test_out;

logic [21:0] cycle; // 2 ^ 22 => ~4,000,000 cycles = 0.08s of "real time"
logic [7:0] numerr;

logic [2:0] test_num;
assign test_num = cycle / 250_000;

debounce dut(.clk(clk), .rst(rst), .bouncy_in(test_in), .clean_out(test_out));

initial begin
    clk = 0;
    cycle = 0;
    rst = 1;
    test_in = 0;
    numerr = 0;
    #20 rst = 0;             // release reset
end

task check(logic expected);
    if (test_out !== expected)
    begin
        numerr <= numerr + 1;
        $display("%d: In test %d: expected %b, got %b.", cycle, test_num,
            expected, test_out);
    end
endtask;

always #10 clk = !clk;

always @(posedge clk)
begin
    cycle <= cycle + 1;

    case (cycle)
    // Test 1 -- simple raise
        250_000: test_in <= 0;
        300_000: test_in <= 1;
        350_000: check(1);
    // Test 2 -- raise then dip
        500_000: test_in <= 0;
        550_000: test_in <= 1;
        600_000: begin
            test_in <= 0;
            check(1);
        end
        605_000: check(1);
        650_000: check(0);
    // Test 3 -- three blips
        750_000: test_in <= 0;
        800_000: test_in <= 1;
        850_000: begin
            test_in <= 0;
            check(1);
        end
        855_000: begin
            test_in <= 1;
            check(1);
        end
        860_000: begin
            test_in <= 0;
            check(1);
        end
        865_000: begin
            test_in <= 1;
            check(1);
        end
        870_000: begin
            test_in <= 0;
            check(1);
        end
        875_000: begin
            check(1);
        end
    // Test 4 -- downards
        1_000_000: test_in <= 1;
        1_050_000: test_in <= 0;
        1_100_000: check(0);

    // Test 5 -- downwards with blip
        1_250_000: test_in <= 1;
        1_300_000: test_in <= 0;
        1_350_000: begin
            test_in <= 1;
            check(0);
        end
        1_355_000: begin
            test_in <= 0;
            check(0);
        end
        1_400_000: check(0);
        1_450_000: check(0);

        1_500_000: begin
            $display("Finishing.");
            if (numerr == 0)
                $display("No errors. Tests passed!");
            else
                $display("%d errors! Tests failed", numerr);
            $finish();
        end

    endcase
end

endmodule
