fun eapproxb(n:int, c:int, x:real) : real= 
	if (n+1 = c) then
		0.0
	else
		x + eapproxb(n, (c+1), (x*(1.0/real(c))));

fun eapprox(n:int) : real = eapproxb(n, 1, 1.0);
		
fun expb(n:int, c:int, x:real, z:real) : real = 
	if (n+1 = c) then
		0.0
	else
		x + expb(n, (c+1), (x*(z/real(c))), z);

fun exp(z:real, n:int) : real = expb(n, 1, 1.0, z);

fun gcd(a:int, b:int) : int = 
	if (a=b) then a else
	if (((a mod 2) = 0) andalso ((b mod 2) = 0)) then 2*gcd(a div 2, b div 2) else
	if (((a mod 2) + (b mod 2)) = 1) then 
		if ((a mod 2) = 1) then gcd(a, b div 2) else gcd(a div 2, b) else 
	if (((a mod 2) + (b mod 2)) = 2) then 
		if (a>b) then gcd(b, ((a div 2)-(b div 2))) else gcd(a, ((b div 2)-(a div 2))) else
	0;
		