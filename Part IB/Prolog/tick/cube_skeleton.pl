%
% Solve a foam puzzle.
%

% piece(?P)
% True if P is an encoded piece of the puzzle
% TODO: add comment explaining how it works
% TODO: implement predicate

% rotate_left(+A, +N, ?B)
% True if the list B is the list A rotated left by N elements
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% reverse(?A, ?B)
% True of all elements in A are in the reverse order to those in B
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% xor(?A, ?B)
% A and B can be 0 for false or 1 for true. xor(A,B) should succeed if
% A xor B is true.
% TODO: add comment explaining how it works
% TODO: implement predicate

% xorlist(?A, ?B)
% True if the pairwise xor of all elements in A and B is true.
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% range(+Min, +Max, -Val)
% Unifies Val with Min on the first evaluation and then all values up
% to Max - 1 on backtracking.
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% orientation(+P, ?O, -OP) 
% Succeeds if OP is P in orientation O. Orientation O is the piece
% rotated O times *anti-clockwise*. Orientation -O is the piece
% flipped then in orientation O
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% compatible(+P1, +Side1, +P2, +Side2)
% Succeeds if Side1 of Piece1 can be plugged into Side2 of
% Piece2. 
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% compatible_corner(+P1, +Side1, +P2, +Side2, +P3, +Side3)
% True if the first corner of sides Side1, Side2, and Side3 of P1, P2, and P3
% lead to at most one finger.
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads

% puzzle(+Ps, ?S).  
% Given Ps as a list of puzzle pieces in the foam cube, unifies S with
% with the solution of the puzzle
% TODO: add comment explaining how it works
% TODO: implement predicate
% TODO: add test cases which are executed when this file loads
