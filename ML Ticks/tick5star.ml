type color = int*int*int   (* RGB colour components, 0..255 *)
type xy = int*int       (* points (x, y) and sizes (w, h) *)
datatype image = Image of xy * color array array;

fun image ((w,h):xy) (c:color) =
        Image((w,h),
            Array.tabulate(h,
                fn i => Array.tabulate(w, fn i => c)));

fun size (Image((w,h),_)) = (w,h):xy;

fun data (Image(_,ar)) = ar;

fun drawPixel (Image((w,h),ar)) (c:color) ((x,y):xy) =
        Array.update(Array.sub(ar, y), x, c);

fun format4 i = StringCvt.padLeft #" " 4 (Int.toString i);

fun pixelToString ((r,g,b):color) = (format4 r)^(format4 g)^(format4 b);

fun printPixel oc v = TextIO.output(oc, pixelToString v);

fun printLine oc ar =
    let in
        Array.app (fn v => (printPixel oc) v) ar;
        TextIO.output(oc, "\n")
    end;

fun toPPM image filename =
    let val oc = TextIO.openOut filename
        val (w,h) = size image
        val data = data image;
    in
        TextIO.output(oc,"P3\n" ^ Int.toString w ^ " " ^ Int.toString h ^ "\n255\n");
        (* code to output image rows, one per line goes here *)
        Array.app (printLine oc) data;
        TextIO.closeOut oc
    end;

fun drawHoriz image colour ((sx,sy):xy) length =
    let in
        if not(length = 0) then
            (drawPixel image colour (sx,sy);
            drawHoriz image colour (sx+1,sy) (length-1))
        else
            ()
    end;

fun drawVert image colour ((sx,sy):xy) length =
    let in
        if not(length = 0) then
            (drawPixel image colour (sx,sy);
            drawVert image colour (sx,sy+1) (length-1))
        else
            ()
    end;

fun drawDiag image colour ((sx,sy):xy) length =
    let in
        if not(length = 0) then
            (drawPixel image colour (sx,sy);
            drawDiag image colour (sx+1,sy+1) (length-1))
        else
            ()
    end;

fun drawLine image colour ((ix, iy):xy) ((fx, fy):xy) =
    let val dx = Int.abs(ix - fx)
        val dy = Int.abs(iy - fy)
        val sx = if ix < fx then 1 else ~1
        val sy = if iy < fy then 1 else ~1
        fun drawLineStep (x, y) err = 
            let val xb = (2*err) < dx
                val yb = (2*err) > (~ dy)
                val nx = if xb then x + sx else x
                val ny = if yb then y + sy else y
                val derrx = if xb then dx else 0
                val derry = if yb then ~ dy else 0
                val nerr = err + derrx + derry
            in
                drawPixel image colour (x,y);
                if (x = fx andalso y = fy) then ()
                else drawLineStep (nx, ny) nerr
            end;
    in
        drawLineStep (ix, iy) (dx-dy)
    end;

fun drawAll f im = 
    let val x = ref 0
        val y = ref 0
        val (w,h) = size im
    in
        while (!y < h) do (
            while (!x < w) do (
                drawPixel im (f((!x, !y):xy)) (!x, !y);
                x := !x + 1);
            y := !y + 1)
    end;

fun gradient ((x,y):xy) : color =
    (((x div 30) * 30) mod 256, 0, ((y div 30) * 30) mod 256);

fun gradImage () = 
    let val im = image (640, 480) (0,0,0)
    in
        drawAll (gradient) (im);
        toPPM im "gradient.ppm"
    end;

fun mandelbrot maxIter (x,y) =
    let fun solve (a,b) c = 
        if c = maxIter then 1.0
        else
            if (a*a + b*b <= 4.0) then 
                solve (a*a - b*b + x,2.0*a*b + y) (c+1)
            else (real c)/(real maxIter)
    in
        solve (x,y) 0
    end;

fun chooseColour (n:real) : color=
    let
        val r = round ((Math.cos n) * 255.0)
        val g = round ((Math.cos n) * 255.0)
        val b = round ((Math.sin n) * 255.0)
    in
        (r,g,b)
    end;

fun rescale ((w,h):xy) (cx,cy,s) ((x,y):xy) =
    let
        val p = s*(real(x)/real(w) - 1.0/2.0) + cx
        val q = s*(real(y)/real(h) - 1.0/2.0) + cy
    in
        (p,q)
    end;

fun compute ((w,h):xy) (cx,cy,s) maxIter = 
    let val im = image (w,h) (0,0,0)
    in
        drawAll 
            (fn (x,y) => 
                chooseColour (mandelbrot maxIter (rescale (w,h) (cx,cy,s) (x,y)))) 
            im;
        toPPM im "mandelbrot.ppm"
    end;

gradImage();
        compute (1000,1000) (~0.74364990,0.13188204,0.00073801) 5;
