datatype 'a tree = Lf
                 | Br of 'a * 'a tree * 'a tree;

fun tcons (v, Lf) = Br (v, Lf, Lf)
    | tcons (v, Br (w, t1, t2)) = Br (v, tcons(w, t2), t1);

fun arrayoflist([]) = Lf
    | arrayoflist(x::xs) = tcons(x, arrayoflist(xs));

exception Subscript;

fun sub (Lf, _) = raise Subscript
    | sub (Br(v, t1, t2), k) =
        if (k=1)
        then v
        else if ((k mod 2) = 0)
            then sub (t1, k div 2)
            else sub (t2, k div 2);

fun listofarray(Lf) = []
    | listofarray(Br(v,t1,t2)) =
    let fun listofarrayb(Lf, _) = []
            | listofarrayb(Br(v,t1,t2), c:int) =
                sub(Br(v,t1,t2), c)::listofarrayb(Br(v,t1,t2), c+1)
                    handle Subscript => []
    in
    listofarrayb(Br(v,t1,t2), 1)
    end;

fun even(a, b) = (sub(a, b) mod 2) = 0;

fun getSubsOfEvens(Lf) = []
    | getSubsOfEvens(Br(v, t1, t2)) =
    let fun getSubsOfEvensB(Lf, _) = []
            | getSubsOfEvensB(Br(v,t1,t2), c) =
                if even(Br(v,t1,t2),c)
                then c::getSubsOfEvensB(Br(v,t1,t2), c+1)
                    handle Subscript => [c]
                else getSubsOfEvensB(Br(v, t1, t2), c+1)
                    handle Subscript => []
    in
    getSubsOfEvensB(Br(v,t1,t2),1)
    end;
