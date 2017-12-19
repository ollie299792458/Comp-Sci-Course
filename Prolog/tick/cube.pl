%
% Solve a foam puzzle.
%

% for all test cases without a printed output, the fact they dont raise and error and terminate counts as a success

% piece(?P)
% True if P is an encoded piece of the puzzle
% Explanation: Simply a list of pieces in the representation described in 2, a label, followed by 4 sides
piece(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]]).
piece(['65', [[1,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]]).
piece(['13', [[0,1,0,1,0,1], [1,1,0,1,0,1], [1,1,0,0,1,1], [1,1,0,0,1,0]]]).
piece(['Cc', [[0,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,0,1,0], [0,0,1,1,0,0]]]).
piece(['98', [[1,1,0,0,1,0], [0,1,0,0,1,0], [0,0,1,1,0,0], [0,0,1,1,0,1]]]).
piece(['02', [[0,0,1,1,0,0], [0,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0]]]).

% rotate_left(+A, +N, ?B)
% True if the list B is the list A rotated left by N elements
% append works as per the lecture notes
append([],L,L).
append([X|T],L,[X|R]) :- append(T,L,R).
% single rotate defined to make multi rotate clearer
rotate([H|T],R) :- append(T,[H],R).
% a list rotated left 0 times is itself
rotate_left(A, 0, A).
% a list rotated left n times is a list B if it can be rotated left once to form a list, which when rotated left n-1 times is b, with a check at the start to make it orthogonal to the first rotate_left (and to make it false for negative numbers)
rotate_left(A, N, B) :- N > 0, M is N-1, rotate(A,C), rotate_left(C, M, B).
% test cases, 0, all the way round, part way, false, and find the list
:- format("Testing rotate_left:").
:- rotate_left([1,2,3], 0, [1,2,3]).
:- rotate_left([1,2,3,4], 4, [1,2,3,4]).
:- rotate_left([1,2,3], 1, [2,3,1]).
:- not(rotate_left([1,2,3,4], 3, [1,2,3,4])).
:- rotate_left([1,2,3,4], 2, A), print(A).
:- format('~n').

% reverse(?A, ?B)
% True of all elements in A are in the reverse order to those in B
% an empty list reversed is an empty list
reverse([],[]).
% the reverse of a list with head A and tail As is the A appended to the end of the reverse of As
reverse([A|As], B) :- reverse(As, C), append(C, [A], B).
% test cases, empty, 1 length, 4 length, false, and find (both ways)
:- format("Testing reverse:").
:- reverse([],[]).
:- reverse([1],[1]).
:- reverse([1,2,3,4],[4,3,2,1]).
:- not(reverse([1,2,3],[3,1,2])).
:- reverse([1,2,3],B), print(B).
:- reverse(A,[1,2,3,4]), print(A). % to demonstrate clauses are orthogonal
:- format('~n').

% xor(?A, ?B)
% A and B can be 0 for false or 1 for true. xor(A,B) should succeed if
% A xor B is true.
% xor is true if A is 1 and B is 0, or vis versa, due to the closed world assumption all other cases are automatically false
xor(1, 0).
xor(0, 1).

% xorlist(?A, ?B)
% True if the pairwise xor of all elements in A and B is true.
% An empty list xored is an empty list
xorlist([],[]).
% a list can be xored if the heads can be xored, and the the tails are xor lists
xorlist([A|As],[B|Bs]) :- xor(A,B),xorlist(As,Bs).
% test cases, true (trivial and complex), false, find (both ways)
:- format("Testing xorlist:").
:- xorlist([],[]).
:- xorlist([1,0,1,0],[0,1,0,1]).
:- not(xorlist([1,1,1,1],[0,0,1,0])).
:- xorlist([1,1,0,1],B),print(B).
:- xorlist(A,[0,1,0,0]),print(A).
:- format('~n').

% range(+Min, +Max, -Val)
% Unifies Val with Min on the first evaluation and then all values up
% to Max - 1 on backtracking.
% two non-orthogonal clauses, first one simply evaluates Val to Min, the second one increases Min, the first clause is unified, then via backtracking min is increased, and then first clause unified again. Also includes range checks on Min and Max
range(Min, Max, Val) :- Min < Max, Val is Min.
range(Min, Max, Val) :- Min < Max, N is Min + 1, range(N, Max, Val).
% test cases, find (trivial, complex positive, and complex negative), invalid
:- format("Testing range:").
:- forall(range(0,0,V),print(V)).
:- forall(range(2,10,V),print(V)).
:- forall(range(-10,2,V),print(V)).
:- not(range(0,-2,_)).
:- format('~n').

% flipped(+P, ?FP)
% Succeeds iff piece FP is piece P flipped
% FP is P flipped if it has the same label, its left and right sides are swapped, and its top and bottom are reversed
flipped([Lbl, [T,R,B,L]],[Lbl, [NT, L, NB, R]]) :- reverse(T, NT), reverse(B, NB).
% test cases, test (true and false), find
:- format("Testing flipped:").
:- flipped(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], ['74', [[0,1,0,0,1,1], [0,1,0,0,1,1] , [0,1,0,0,1,0], [0,1,0,1,0,0]]]).
:- not(flipped(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], ['74', [[1,1,0,0,1,1], [0,1,0,0,1,1] , [1,1,0,0,1,0], [0,1,0,1,0,0]]])).
:- flipped(['65', [[1,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]], FP), print(FP).
:- format('~n').

% orientation(+P, ?O, -OP) 
% Succeeds iff OP is P in orientation O. Orientation O is the piece
% rotated O times *anti-clockwise*. Orientation -O is the piece
% flipped then in orientation O
% this is simply a self explanatory list with static Os, tried something more concise, but couldnt get it to work in the O- case due to prologs arithmetic
orientation(P, 0, P).
orientation([Lbl, P], 1, [Lbl, OP]) :- rotate_left(P, 1, OP).
orientation([Lbl, P], 2, [Lbl, OP]) :- rotate_left(P, 2, OP).
orientation([Lbl, P], 3, [Lbl, OP]) :- rotate_left(P, 3, OP).
orientation(P, -1, OP) :- flipped(P, NP), orientation(NP, 1, OP).
orientation(P, -2, OP) :- flipped(P, NP), orientation(NP, 2, OP).
orientation(P, -3, OP) :- flipped(P, NP), orientation(NP, 3, OP).
orientation(P, -4, OP) :- flipped(P, OP). % rotation by 4 is no change
% these deal with cases outside of -4 to 3 by subtracting or adding whole 360 degree rotations until it is in the range (in the event negative, it will end up in the range -4 to -1, and in the event positive it will end up in the range 0 to 3), nonvar(O) results in this only unifying in the +O case, and not unifying at all in the -O case.
orientation(P, O, OP) :- nonvar(O), O < -4, NO is O + 4, orientation(P, NO, OP).
orientation(P, O, OP) :- nonvar(O), O > 3, NO is O - 4, orientation(P, NO, OP).
% test cases (true, false, get O, get OP, get Os and OPs)
:- format("Testing orientation:").
:- orientation(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], -2, ['74', [[0,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,1], [0,1,0,0,1,1]]]).
:- not(orientation(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], -2, ['74', [[0,1,0,1,0,0], [0,1,0,0,1,0], [1,1,0,0,1,0], [0,1,0,0,1,1]]])).
:- orientation(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], O, ['74', [[0,1,0,1,0,0], [0,1,0,0,1,1], [0,1,0,0,1,1], [0,1,0,0,1,0]]]), print(O).
:- orientation(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], 3, OP),print(OP).
:- forall(orientation(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], O, _),print(O)).
:- format('~n').

% compatible(+P1, +Side1, +P2, +Side2)
% Succeeds if Side1 of Piece1 can be plugged into Side2 of
% Piece2. 
% helper predicate
without_last([_],[]).
without_last([X|Xs], [X|Ys]) :- without_last(Xs, Ys).
without_first_and_last([_|Xs], Ys) :- without_last(Xs, Ys).
% sides 0 are compatible if you can xorlist them, remembering to remove the head and last element first (dealing with those is compatible_corners problem), head and tail removed by without_first_and_last, then need to reverse one of the lists (else youre comparing an edge with a flipped edge, rather than an edge with an edge).
compatible([_,[E1,_,_,_]], 0, [_,[E2,_,_,_]], 0) :- without_first_and_last(E1, N1), without_first_and_last(E2, X2), reverse(N1,X1), xorlist(X1,X2).
% for all other cases, rotate the piece until the side in question is at side 0, and then check again if they are compatible, corners are handled by the next predicate, have to be made orthogonal, else puzzle does loads of backtracking and ending up at the same solution
compatible(P1, S1, P2, S2) :- S1 > 0, orientation(P1, S1, NP1), compatible(NP1,0,P2,S2).
compatible(P1, S1, P2, S2) :- S1 == 0, S2 > 0, orientation(P2, S2, NP2), compatible(P1, S1, NP2, 0).
% test cases (example in notes edited so it doesnt require a reversal, edited to be asymetrical, edited to show that corners are handled properly, and edited so it is false
:- format("Testing compatible:").
:- compatible(['65', [[1,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]],1,['13R', [[0,1,0,1,0,1], [1,1,0,1,0,1], [1,1,0,0,1,1], [0,1,0,0,1,1]]],3).
:- compatible(['65A', [[1,1,0,0,1,1], [1,0,1,1,1,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]],1,['13A', [[0,1,0,1,0,1], [1,1,0,1,0,1], [1,1,0,0,1,1], [0,0,0,0,1,1]]],3).
:- compatible(['65', [[1,1,0,0,1,1], [0,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]],1,['13C', [[0,1,0,1,0,1], [1,1,0,1,0,1], [1,1,0,0,1,1], [0,1,0,0,1,1]]],3).
:- not(compatible(['65', [[1,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]],1,['13X', [[0,1,0,1,0,1], [1,1,0,1,0,1], [1,1,0,0,1,1], [0,1,0,0,0,0]]],3)).
:- format('~n').

% compatible_corner(+P1, +Side1, +P2, +Side2, +P3, +Side3)
% True if the first corner of sides Side1, Side2, and Side3 of P1, P2, and P3
% lead to at most one finger.
% corners of sides 0 are compatible iff only one of them is 1 (three predicates, for each possibility of 1)
compatible_corner([_,[[F1|_]|_]],0,[_,[[F2|_]|_]],0,[_,[[F3|_]|_]],0) :- F1 = 1, F2 = 0, F3 = 0.
compatible_corner([_,[[F1|_]|_]],0,[_,[[F2|_]|_]],0,[_,[[F3|_]|_]],0) :- F1 = 0, F2 = 1, F3 = 0.
compatible_corner([_,[[F1|_]|_]],0,[_,[[F2|_]|_]],0,[_,[[F3|_]|_]],0) :- F1 = 0, F2 = 0, F3 = 1.
% for all other cases, orientate each piece in turn so that the side in question is on side 0, like with compatible, have to be semi-orthogonal so they behave well under backtracking
compatible_corner(P1, S1, P2, S2, P3, S3) :- S1 > 0, orientation(P1, S1, NP1), compatible_corner(NP1,0,P2,S2,P3,S3).
compatible_corner(P1, S1, P2, S2, P3, S3) :- S1 == 0, S2 > 0, orientation(P2, S2, NP2), compatible_corner(P1,S1,NP2,0,P3,S3).
compatible_corner(P1, S1, P2, S2, P3, S3) :- S1 == 0, S2 == 0, S3 > 0, orientation(P3, S3, NP3), compatible_corner(P1,S1,P2,S2,NP3,0).
% test cases, true (simple, and with rotations), and false
:- format("Testing compatible_corner:").
:- compatible_corner(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], 0, ['74B', [[0,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]],0,['74B', [[0,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]],0).
:- compatible_corner(['74A', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [1,1,0,0,1,1]]], 3, ['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]],1,['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]],2).
:- not(compatible_corner(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], 0, ['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]],0,['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], 0)).
:- format('~n').

% puzzle(+Ps, ?S).  
% Given Ps as a list of puzzle pieces in the foam cube, unifies S with
% with the solution of the puzzle
% a helper predicate, and orthogonal generate and test predicates, for the -S and +S cases

% helper predicate (makes it possible to use range to subscript lists - in fact, this aleviates any need for range)
unify_piece([P,_,_,_,_,_],0,P).
unify_piece([_,P,_,_,_,_],1,P).
unify_piece([_,_,P,_,_,_],2,P).
unify_piece([_,_,_,P,_,_],3,P).
unify_piece([_,_,_,_,P,_],4,P).
unify_piece([_,_,_,_,_,P],5,P).

% works by trying to add pieces to a solution to the puzzle one by one, backtracking on failure, generate and test piece by piece
puzzle(Ps, [[P0,O0],[P1,O1],[P2,O2],[P3,O3],[P4,O4],[P5,O5]]) :- 
% make it so this only unifies in the generate case
var(P0), var(P1), var(P2), var(P3), var(P4), var(P5), var(O0), var(O1), var(O2), var(O3), var(O4), var(O5),
% set the first piece to position 0 with orientation 0
unify_piece(Ps, 0, P0), O0 = 0, OP0 = P0, N0 = 0,
% set up the second piece (backtracking included), assuring its location is unique
unify_piece(Ps,N1,P1), not(member(N1, [N0])),orientation(P1,O1,OP1),
% get it so the second piece fits with all pieces beforehand (the first piece)
compatible(OP0,2,OP1,0),
% same for the third piece
unify_piece(Ps,N2,P2), not(member(N2,[N0, N1])), orientation(P2,O2,OP2),
compatible(OP0,3,OP2,0), compatible(OP1,3,OP2,1), compatible_corner(OP0,3,OP1,0,OP2,1),
% and the fourth
unify_piece(Ps,N3,P3), not(member(N3,[N0, N1,N2])), orientation(P3,O3,OP3),
compatible(OP0,1,OP3,0),compatible(OP1,1,OP3,3),compatible_corner(OP0,2,OP1,1,OP3,0),
% fifth
unify_piece(Ps,N4,P4), not(member(N4,[N0, N1,N2,N3])), orientation(P4,O4,OP4),
compatible(OP1,2,OP4,0), compatible(OP2,2,OP4,3), compatible(OP3,2,OP4,1),
compatible_corner(OP2,2,OP1,3,OP4,0), compatible_corner(OP3,3,OP1,2,OP4,1),
% sixth
unify_piece(Ps,N5,P5), not(member(N5,[N0, N1,N2,N3,N4])), orientation(P5,O5,OP5),
compatible(OP4,2,OP5,0), compatible(OP2,3,OP5,3), compatible(OP0,0,OP5,2), compatible(OP3,1,OP5,1),
compatible_corner(OP5,0,OP4,3,OP2,3), compatible_corner(OP5,1,OP4,2,OP3,2), compatible_corner(OP5,2,OP0,1,OP3,1), compatible_corner(OP5,3,OP0,0,OP2,0).

% test predicate, in order to meet the requirement that solutions where P0 is not in position 0 and orientation 0 in the given solution are still seen as valid solutions
puzzle(Ps, [[P0,O0],[P1,O1],[P2,O2],[P3,O3],[P4,O4],[P5,O5]]) :-
% make it so this only unifies in the test case
nonvar(P0), nonvar(P1), nonvar(P2), nonvar(P3), nonvar(P4), nonvar(P5), nonvar(O0), nonvar(O1), nonvar(O2), nonvar(O3), nonvar(O4), nonvar(O5),
% check all pieces are pieces
member(P0, Ps), member(P1, Ps), member(P2, Ps), member(P3, Ps), member(P4, Ps), member(P5, Ps),
% get orientated pieces
orientation(P0, O0, OP0), orientation(P1, O1, OP1), orientation(P2, O2, OP2), orientation(P3, O3, OP3), orientation(P4, O4, OP4), orientation(P5, O5, OP5),
% test all edges
compatible(OP0,2,OP1,0), compatible(OP0,3,OP2,0), compatible(OP1,3,OP2,1),compatible(OP0,1,OP3,0), compatible(OP1,1,OP3,3), compatible(OP1,2,OP4,0), compatible(OP2,2,OP4,3),compatible(OP3,2,OP4,1), compatible(OP4,2,OP5,0), compatible(OP2,3,OP5,3), compatible(OP0,0,OP5,2), compatible(OP3,1,OP5,1),
% test all corners
compatible_corner(OP0,3,OP1,0,OP2,1), compatible_corner(OP0,2,OP1,1,OP3,0), compatible_corner(OP2,2,OP1,3,OP4,0), compatible_corner(OP3,3,OP1,2,OP4,1), compatible_corner(OP5,0,OP4,3,OP2,3), compatible_corner(OP5,1,OP4,2,OP3,2), compatible_corner(OP5,2,OP0,1,OP3,1), compatible_corner(OP5,3,OP0,0,OP2,0).

% test cases (simple case, full case, simple multisolution, check case)
:- format("Testing puzzle:").
% A1 at 0, A2 at 0, B1 at 0, B2 at 0, A3 at 0, B3 at 0
:- puzzle([['A1', [[1,0],[0,1],[1,0],[0,1]]], ['A2', [[1,0],[0,1],[1,0],[0,1]]], ['B1', [[0,0],[0,0],[0,0],[0,0]]], ['B2', [[0,0],[0,0],[0,0],[0,0]]], ['A3', [[1,0],[0,1],[1,0],[0,1]]], ['A4', [[1,0],[0,1],[1,0],[0,1]]]], [[P0,O0],[P1,O1],[P2,O2],[P3,O3],[P4,O4],[P5,O5]]), format('~w at ~w~n', [P0, O0]), format('~w at ~w~n', [P1, O1]),format('~w at ~w~n', [P2, O2]), format('~w at ~w~n', [P3, O3]), format('~w at ~w~n', [P4, O4]), format('~w at ~w~n', [P5, O5]).
:- puzzle([['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]], ['65', [[1,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,1,0,1]]], ['13', [[0,1,0,1,0,1], [1,1,0,1,0,1], [1,1,0,0,1,1], [1,1,0,0,1,0]]], ['Cc', [[0,0,1,1,0,0], [0,0,1,1,0,0], [0,1,0,0,1,0], [0,0,1,1,0,0]]], ['98', [[1,1,0,0,1,0], [0,1,0,0,1,0], [0,0,1,1,0,0], [0,0,1,1,0,1]]], ['02', [[0,0,1,1,0,0], [0,1,0,0,1,1], [1,0,1,1,0,0], [0,0,1,1,0,0]]]], [[P0,O0],[P1,O1],[P2,O2],[P3,O3],[P4,O4],[P5,O5]]), format('~w at ~w~n', [P0, O0]), format('~w at ~w~n', [P1, O1]),format('~w at ~w~n', [P2, O2]), format('~w at ~w~n', [P3, O3]), format('~w at ~w~n', [P4, O4]), format('~w at ~w~n', [P5, O5]).
:- forall(puzzle([['A1', [[1,0],[0,1],[1,0],[0,1]]], ['A2', [[1,0],[0,1],[1,0],[0,1]]], ['B1', [[0,0],[0,0],[0,0],[0,0]]], ['B2', [[0,0],[0,0],[0,0],[0,0]]], ['A3', [[1,0],[0,1],[1,0],[0,1]]], ['A4', [[1,0],[0,1],[1,0],[0,1]]]], _), piece(['74', [[1,1,0,0,1,0], [0,1,0,1,0,0], [0,1,0,0,1,0], [0,1,0,0,1,1]]])).
:- Ps = [['A1', [[1,0],[0,1],[1,0],[0,1]]], ['A2', [[1,0],[0,1],[1,0],[0,1]]], ['B1', [[0,0],[0,0],[0,0],[0,0]]], ['B2', [[0,0],[0,0],[0,0],[0,0]]], ['A3', [[1,0],[0,1],[1,0],[0,1]]], ['A4', [[1,0],[0,1],[1,0],[0,1]]]], P0 = ['A1', [[1, 0], [0, 1], [1, 0], [0, 1]]], O0 = O1, O1 = O2, O2 = O3, O3 = O4, O4 = O5, O5 = 0, P1 = ['A2', [[1, 0], [0, 1], [1, 0], [0, 1]]], P2 = ['B1', [[0, 0], [0, 0], [0, 0], [0, 0]]], P3 = ['B2', [[0, 0], [0, 0], [0, 0], [0, 0]]], P4 = ['A3', [[1, 0], [0, 1], [1, 0], [0, 1]]], P5 = ['A4', [[1, 0], [0, 1], [1, 0], [0, 1]]], S = [[P0,O0],[P1,O1],[P2,O2],[P3,O3],[P4,O4],[P5,O5]], rotate_left(Ps,2,RPs), puzzle(RPs, S).
:- format('The wait just then was due to many solutions to the simple puzzle being found - see source~n').
% add more test case

% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads
