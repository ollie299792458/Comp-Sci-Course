fun gamma (n:int) =
    if (n = 0)
    then (1.0 + Math.sqrt(5.0)/2.0)
    else (1.0 / (gamma(n-1)-1.0));

fun power (x:int, n:int, t:int) =
    if (n = 0)
    then (t)
    else (power(x,n-1,t*x));

fun sumR [] = 0
    | sumR (x::l) = x+sumR(l);

fun sumI ([], t) = t
    | sumI (x::l, t) = sumI(l, t + x);

fun last (x::[]) = x
    | last (x::l) = last(l);

fun evens ([]) = []
    | evens (x::l) =
    let fun evensb([], _, t) = t
        | evensb(x::l, false, t) = evensb(l, true, t)
        | evensb(x::l, true, t) = evensb(l, false, x::t);
    in evensb(l, true, []) end;

fun member (x, []) = false
    | member(x, h::l) =
        (x = h) orelse member(x,l);

fun union ([], x) = x
    | union (x, []) = x
    | union (x,y) =
    let fun unionb ([], y, t) = t@y
            | unionb (xh::xl, y, t) =
            if member(xh,y)
            then unionb(xl, y, t)
            else unionb(xl, y, xh::t);
    in unionb(x,y,[]) end;
