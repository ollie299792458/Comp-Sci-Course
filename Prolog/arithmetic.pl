prim(0,z).
prim(A,s(B)) :- C is A-1,A>0,prim(C,B).

plus(z,z,z).
plus(z,s(B),s(C)) :- plus(z,B,C).
plus(s(A),B,s(C)) :- plus(A,B,C).

mult(z,z,z).
mult(z,_,C) :- prim(0,C).
mult(s(A),B,C) :- mult(A,B,D),plus(B,D,C).

