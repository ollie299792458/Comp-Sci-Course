/*******************************************************************************
Copyright (c) 2016, Robert Eady
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*******************************************************************************/


`timescale 1ns/10ps

module clarvi_sim();
    logic clock = 1;
    logic reset = 0;

    localparam ADDR_WIDTH = 29;
    localparam MEM_WIDTH  = 14;

    logic [ADDR_WIDTH-1:0] main_address;
    logic [3:0]  main_byteenable;
    logic        main_read;
    logic [31:0] main_readdata;
    logic        main_write;
    logic [31:0] main_writedata;
    logic        main_waitrequest;
    logic        main_readdatavalid = 1;
    logic [ADDR_WIDTH-1:0] instr_address;
    logic        instr_read;
    logic [31:0] instr_readdata;
    logic        instr_waitrequest = 0;
    logic        instr_readdatavalid = 1;

    logic interrupt = 0;

    
    logic read_from_external;
    logic external_access;
    logic [31:0] external_readdata;
    logic [31:0] internal_readdata;
    

    dual_port_bram #(
        .ADDRESS_WIDTH(MEM_WIDTH)
    ) mem (
        .clock,
        .reset,
        .avs_a_address      (main_address[MEM_WIDTH-1 : 0]),
        .avs_a_byteenable   (main_byteenable),
        .avs_a_read         (main_read),
        .avs_a_readdata     (internal_readdata),
        .avs_a_write        (main_write & !external_access),
        .avs_a_writedata    (main_writedata),
        .avs_b_address      (instr_address[MEM_WIDTH-1 : 0]),
        .avs_b_byteenable   (4'b1111),
        .avs_b_read         (instr_read),
        .avs_b_readdata     (instr_readdata),
        .avs_b_write        (1'b0),
        .avs_b_writedata    (32'dx)
    );

    // components for mocking interrupt or waitrequest signals:
    // mock_waitrequest wr(main_waitrequest, instr_waitrequest);
    // mock_interrupt mi(interrupt);
    
    always_comb begin
        main_readdata = read_from_external ? external_readdata : internal_readdata;
	// it's an external access if any address bit larger than the memory is non-zero
        external_access = |(main_address[ADDR_WIDTH - 1: MEM_WIDTH]);
    end
    
    clarvi_avalon_external #(
	.ADDRESS_WIDTH(ADDR_WIDTH)
	) avalon0 (
            .clk(clock),
            .reset(reset),
            .avs_mm_slave_address(main_address),
            .avs_mm_slave_readenable(main_read),
            .avs_mm_slave_readdata(external_readdata),
            .avs_mm_slave_readdatavalid(read_from_external),
            .avs_mm_slave_waitrequest(main_waitrequest),
            .avs_mm_slave_writeenable(main_write),
            .avs_mm_slave_writedata(main_writedata),
            .avs_mm_slave_byteena(main_byteenable)
    );

    clarvi_avalon #(
        .INSTR_ADDR_WIDTH(ADDR_WIDTH),
        .DATA_ADDR_WIDTH(ADDR_WIDTH)
    ) clarvi (
        .clock,
        .reset,
        .avm_main_address        (main_address),
        .avm_main_byteenable     (main_byteenable),
        .avm_main_read           (main_read),
        .avm_main_readdata       (main_readdata),
        .avm_main_write          (main_write),
        .avm_main_writedata      (main_writedata),
        .avm_main_waitrequest    (main_waitrequest),
        .avm_main_readdatavalid  (main_readdatavalid),
        .avm_instr_address       (instr_address),
        .avm_instr_read          (instr_read),
        .avm_instr_readdata      (instr_readdata),
        .avm_instr_waitrequest   (instr_waitrequest),
        .avm_instr_readdatavalid (instr_readdatavalid),
        .inr_irq(interrupt),
        .debug_register28(),
        .debug_scratch(),
        .debug_pc()
    );

    initial begin
        forever #5 clock = ~clock;
    end
    
    initial begin
        reset = 1;
        #20 reset = 0;
    end
endmodule



module mock_waitrequest(
    output logic waitrequest1,
    output logic waitrequest2
);
   initial begin
       #2 waitrequest1 = 0;
       forever begin
           #40 waitrequest1 = 1;
           #20 waitrequest1 = 0;
       end
   end

   initial begin
       #2 waitrequest2 = 0;
       forever begin
           #50 waitrequest2 = 1;
           #30 waitrequest2 = 0;
       end
   end
endmodule


module mock_interrupt(
    output logic interrupt
);
    initial begin
        interrupt = 0;
        #50    interrupt = 1;
        #10000 interrupt = 0;
        #500   interrupt = 1;
        #50    interrupt = 0;
    end
endmodule
