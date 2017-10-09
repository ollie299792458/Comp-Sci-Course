datatype 'a stream = Cons of 'a * (unit -> 'a stream);
fun from k = Cons(k, fn() => from(k+1));

fun merge (Cons(x, xs), Cons(y, ys)) = 
        if (x = y)
        then Cons(x, fn() => merge (xs(), ys()))
        else if (x < y)
        then Cons(x, fn() => merge (xs(), Cons(y, ys)))
        else Cons(y, fn() => merge (Cons(x, xs), ys()));

fun pows23from k = merge(
            Cons((2*k), fn() => pows23from (2*k)),
            Cons((3*k), fn() => pows23from (3*k)));

val pows23 = Cons(1,fn() => pows23from 1);

fun pows235from k = merge(pows23from k,
            Cons((5*k), fn() => pows235from (5*k)));

val pows235 = Cons(1, fn() => pows235from 1);

