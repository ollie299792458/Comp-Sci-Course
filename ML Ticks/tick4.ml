fun nfold f 0 = (fn x => x)
    | nfold f n = (fn x => nfold f (n-1) (f x));

fun sum m n = nfold (fn x => x+1) n m;

fun product m n = nfold (fn x => x+m) n 0;

fun power m n = nfold (fn x => x*m) n 1;

datatype 'a stream = Cons of 'a * (unit -> 'a stream);
fun from k = Cons(k, fn() => from(k+1));

fun nth (Cons(x, xf), 0) = x
    | nth (Cons(x, xf, n) =  

