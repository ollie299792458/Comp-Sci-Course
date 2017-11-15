take([H|T],H,T).
take([H|T],R,[H|S]) :- take(T,R,S).

perm([],[]).
perm(L,[H|T]) :- take(L,H,R),perm(R,T).

checkBlue([blue|T]) :- checkBlue(T).
checkBlue([]).

checkWhite([white|T]) :- checkWhite(T).
checkWhite(T):-checkBlue(T).

checkRed([red|T]) :- checkRed(T).
checkRed(T) :- checkWhite(T).

checkColours(A) :- checkRed(A).

flag(In,Out) :- perm(In,Out),checkColours(Out).

