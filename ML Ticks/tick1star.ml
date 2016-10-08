fun eapproxb(n:int, c:int,x:real) = 
	if (n+1 = c) then
		0.0
	else
		x + eapproxb(n, (c+1), (x*(1.0/real(c))));

fun eapprox(n:int) = eapproxb(n, 1, 1.0);

fun power(n:int, x:real) = 
	if (n = 1) then x else 
	if ((n mod 2) = 0) then 
		power(n div 2, x * x) else
		x * power(n div 2, x * x);

fun exp(z:int, n:int) = power(z, eapprox(n));

fun gcd(a:int, b:int) = 
	if (a=b) then a else
	if (((a mod 2) = 0) andalso ((b mod 2) = 0)) then 2*gcd(a div 2, b div 2) else
	if (((a mod 2) + (b mod 2)) = 1) then 
		if ((a mod 2) = 1) then gcd(a, b div 2) else gcd(a div 2, b) else 
	if (((a mod 2) + (b mod 2)) = 2) then 
		if (a>b) then gcd(b, ((a div 2)-(b div 2))) else gcd(a, ((b div 2)-(a div 2))) else
	0;
		