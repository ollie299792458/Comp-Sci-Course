fun min ([]) = 0
    | min(x::xs) =
    let fun minb(x::xs, m) =
                if (x<m)
                then minb (xs, x)
                else minb (xs, m)
            | minb([], m) = m
    in minb(xs, x)
    end;

fun remove(_, []) = []
    | remove(r, x::xs) =
        if (r=x)
        then xs
        else x::remove(r, xs);

fun selection_sort ([]) = []
    | selection_sort (x::xs) =
    let val m = min(x::xs)
    in m::selection_sort(remove(m, x::xs)) end;



fun bubblesortpass([], t, sw) = (rev(t), sw)
    | bubblesortpass(x::[], t, sw) =
        bubblesortpass([], x::t, sw)
    | bubblesortpass(x::y::xs, t, sw) =
        if (y < x)
        then bubblesortpass(x::xs, y::t, true)
        else bubblesortpass(y::xs, x::t, sw);

fun tuplehandler(([],_)) = []
    | tuplehandler((x::xs,sw)) =
        if (sw)
        then tuplehandler(bubblesortpass(x::xs, [], false))
        else x::xs;

fun bubblesort ([]) = []
    | bubblesort(x::xs) =
        tuplehandler((x::xs, true));



datatype 'a tree = Lf
                 | Br of 'a * 'a tree * 'a tree;

fun sumtree Lf = 0
    | sumtree(Br(v, t1, t2)) = v + sumtree(t1) + sumtree(t2);

fun ftree(k,n) =
    if n = 0
    then Lf
    else Br(k, ftree(2*k, n-1), ftree(2*k+1, n-1));



fun minleaf(Lf) = Lf
    | minleaf(Br(v, Lf, _)) = v
    | minleaf(Br(v, t1, t2)) = minleaf(t1);

fun deletemin(Lf) = Lf
    | deletemin(Br(v, Lf, Lf)) = Lf
    | deletemin(Br(v, Lf, t2)) = t2
    | deletemin(Br(v, t1, t2)) = Br(v, deletemin(t1), t2);

fun deletehead(Lf) = Lf
    | deletehead(Br(v, t1, t2)) = 
        Br(minleaf(t2), deletemin(t1), t2);

fun deletenode(Lf, _) = Lf
    | deletenode(Br(v, t1, t2), x) =
        if (v = x)
        then deletehead(Br(v, t1, t2))
        else if (v < x) 
            then Br(v, t1, deletenode(t2, x))
            else Br(v, deletenode(t1, x), t2);




