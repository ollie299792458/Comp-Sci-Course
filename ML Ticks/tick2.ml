fun last [] = nil
    | (x::l) = if (l = nil) then x else last(l);
