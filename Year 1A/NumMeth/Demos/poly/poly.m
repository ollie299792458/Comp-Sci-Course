% Demonstration of noise caused by rounding error during
% different numerical evaluations of (x+1)^7.
% Taken from Cleve Moler's article.

x = 0.988:0.0001:1.012;
% the previous line is bad practice, even though harmless for a graphical
% display -- it's unclear whether x takes 2400 or 2401 values (depending
% on whether 0.988 + 2400*0.0001 > 1.012 or not!).

y = x.^7 - 7*x.^6 + 21*x.^5 - 35*x.^4 + 35*x.^3 - 21*x.^2 + 7*x - 1;
z = (x-1).^7;
plot(x,y, x,z);

