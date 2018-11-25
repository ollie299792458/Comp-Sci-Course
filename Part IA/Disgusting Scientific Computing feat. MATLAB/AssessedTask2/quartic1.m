function [w] = quartic1 (x)
% Computes a quartic function, a = 1, b = 1

a = 1;
b = 1;
w = (1/2)*a*x.^2 + (1/4)*b*x.^4;