function [w] = quartic2(x,a,b)
% Computes a quartic function
w = (1/2)*a*x.^2 + (1/4)*b*x.^4;