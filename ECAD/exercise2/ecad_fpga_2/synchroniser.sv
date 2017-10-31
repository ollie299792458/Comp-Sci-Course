module synchroniser(
    input clk,
    input async,
    output reg sync);
    
    reg metastable;
    always_ff @(posedge clk)
    begin
        metastable <= async;
        sync <= metastable;
    end
endmodule
