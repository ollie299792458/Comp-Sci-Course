fun evalquad (a:real, b:real, c:real, x:real) = a*x*x + b*x + c;

fun facr(n:int) = 
	if (n<0) then 
		0
	else
		(if (n = 0) then 1 else (n*facr(n-1)));

fun facib(n:int, x:int) = 
	if (n=0) then
		x
	else
		facib(n-1,x*n);
		
fun faci(n:int) = facib(n,1);

fun sumtb(n:int, x:real) = 
	if (n = 1) then
		x
	else
		x + sumtb(n-1, x/2.0);

fun sumt(n:int) = sumtb(n, 1.0);