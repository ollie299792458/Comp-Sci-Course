module clarvi_avalon_external #(
    parameter ADDRESS_WIDTH = 26
)(
	input wire        clk,
	input wire        reset,
	input wire [ADDRESS_WIDTH-1:0] avs_mm_slave_address,
	input wire        avs_mm_slave_readenable,
	output reg [31:0] avs_mm_slave_readdata,
	output reg        avs_mm_slave_readdatavalid,
	output wire       avs_mm_slave_waitrequest,
	input wire        avs_mm_slave_writeenable,
	input wire [31:0] avs_mm_slave_writedata,
	input wire [3:0]  avs_mm_slave_byteena
);

	import "DPI-C" function void clarvi_avalon_setup();
	import "DPI-C" function int clarvi_avalon_read_mem(input int address, input byte byteena);
	import "DPI-C" function void clarvi_avalon_write_mem(input int address, input int data, input byte byteena);
	
	assign avs_mm_slave_waitrequest = 0;

	initial begin
            clarvi_avalon_setup();
	end
	
	always @(posedge clk) begin
		if (reset) begin
			avs_mm_slave_readdata <= 0;
			avs_mm_slave_readdatavalid <= 0;
		end else begin
                        if (avs_mm_slave_address > 'hffff) begin
                            if (avs_mm_slave_readenable) begin
                                    avs_mm_slave_readdata <= clarvi_avalon_read_mem(avs_mm_slave_address, avs_mm_slave_byteena);
                                    avs_mm_slave_readdatavalid <= 1;
                            end else
                                    avs_mm_slave_readdatavalid <= 0;
                            
                            if (avs_mm_slave_writeenable) begin
                                    clarvi_avalon_write_mem(avs_mm_slave_address, avs_mm_slave_writedata, avs_mm_slave_byteena);
                            end
                        end
		end
	end
endmodule
