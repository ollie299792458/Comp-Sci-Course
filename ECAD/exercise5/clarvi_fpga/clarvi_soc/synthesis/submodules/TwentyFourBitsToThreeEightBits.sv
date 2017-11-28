module TwentyFourBitsToThreeEightBits (
	    input [23:0] twentyFourBits,
	    output [7:0] eightBitsOne,
	    output [7:0] eightBitsTwo,
	    output [7:0] eightBitsThree
	  );

	  always_comb begin
	    eightBitsOne = twentyFourBits[7:0];
	    eightBitsOne = twentyFourBits[15:8];
	    eightBitsOne = twentyFourBits[23:16];
	  end
endmodule
