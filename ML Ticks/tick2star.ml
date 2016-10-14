fun concat ([], []) = []
    | concat ([], x) = x
    | concat (x, []) = x
    | concat (y, h::l) = concat(h::y, l);

fun allcons (a, [])= []
    | allcons (a, x::l) = (a::x)::allcons(a,l);

fun listnest ([]) = []
    | listnest (h::l) = (h::[])::listnest(l);

fun choose (k, []) = []
    | choose (k, h::l) =
    if (k = 1)
    then listnest(h::l)
    else concat(allcons(h, choose(k-1, l)),choose(k,l));
