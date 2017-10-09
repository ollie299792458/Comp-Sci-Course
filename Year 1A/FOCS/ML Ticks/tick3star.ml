datatype 'a tree = Lf
                 | Br of 'a * 'a tree * 'a tree;

fun insert(a:string, Lf) = Br(a, Lf, Lf)
    | insert(a:string, Br(v, t1, t2)) =
        if (v = a)
        then Br(v, t1, t2)
        else if (v < a)
            then if (t2 = Lf)
                then Br(v, t1, Br(a, Lf, Lf))
                else Br(v, t1, insert(a, t2))
            else if (t1 = Lf)
                then Br(v, Br(a, Lf, Lf), t1)
                else Br(v, insert(a, t1), t2);

fun member(a:string, Lf) = false
    | member(a:string, Br(v, t1, t2)) =
        if (v = a)
        then true
        else if (v < a)
            then member(a, t2)
            else member(a, t1);

fun union(Lf, Lf) = Lf
    | union(Br(v,v1,v2), Lf) = Br(v, v1, v2)
    | union(Lf, Br(u,u1,u2)) = Br(u, u1, u2)
    | union(Br(v, v1, v2), Br(u, u1, u2)) =
        union(union(insert(u, Br(v, v1, v2)), u1), u2);

fun inter(Lf, Lf) = Lf
    | inter(v, u:string tree) = 
        let fun interb(Lf, Lf, t) = t
                | interb(Lf, u, t) = t
                | interb(v, Lf, t) = t
                | interb(Br(v, v1, v2), Br(u, u1, u2), t) =
                    if member(v, Br(v, v1, v2))
                    then interb(v1, Br(u,u1,u2), interb(v2, Br(u,u1,u2), insert(v, t)))
                    else interb(v1, Br(u,u1,u2), interb(v2, Br(u,u1,u2), t))
        in
        interb(v, u, Lf)
        end;

exception NoMinLeaf;

fun minleaf(Lf) = raise NoMinLeaf
    | minleaf(Br(v, Lf, _)) = v
    | minleaf(Br(v, t1, t2)) = minleaf(t1);

fun removemin(Lf) = Lf
    | removemin(Br(v, Lf, Lf)) = Lf
    | removemin(Br(v, Lf, t2)) = t2
    | removemin(Br(v, t1, t2)) = Br(v, removemin(t1), t2);

fun removehead(Lf) = Lf
    |removehead(Br(v, t1, t2)) =
        if (t2 = Lf)
        then t1
        else Br(minleaf(t2), t1, removemin(t2));

fun remove(a:string, Lf) = Lf
    | remove(a:string, Br(v, t1, t2)) =
        if (v = a)
        then removehead(Br(v, t1, t2))
        else if (v < a)
            then Br(v, t1, remove(a, t2))
            else Br(v, remove(a, t1), t2);
