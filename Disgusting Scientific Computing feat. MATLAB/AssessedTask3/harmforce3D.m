function [f] = harmforce3D(x1,x2,k) 
% Computes a harmonic restoring force between a point particle 
% of vector position x1, and another one at x2
% k : spring constant

f =  -k .* ( x1 - x2 ); 