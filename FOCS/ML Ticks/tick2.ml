fun fst (x,y) = x;
fun snd (x,y) = y;

fun last (x::[]) = x
    | last (x::l) = last(l);

fun butlast(x::[]) = []
    | butlast(x::l) = x::butlast(l);

fun nth (x::l,n) =
    if (n = 0)
    then x
    else nth(l,n-1);
