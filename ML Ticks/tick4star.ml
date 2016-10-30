datatype 'a stream = Cons of 'a * (unit -> 'a stream);
fun from k = Cons(k, fn() => from(k+1));

fun merge (Cons(x, xs), Cons(y, ys)) = 
        if (x < y)
        then Cons(x, fn() => merge (xs(), Cons(y, ys)))
        else Cons(y, fn() => merge (Cons(x, xs), ys()));


