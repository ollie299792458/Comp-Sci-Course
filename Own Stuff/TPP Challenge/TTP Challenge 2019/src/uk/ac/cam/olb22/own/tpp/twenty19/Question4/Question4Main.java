package uk.ac.cam.olb22.own.tpp.twenty19.Question4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.cam.olb22.own.tpp.twenty19.Primes;

public class Question4Main
{
	private static Primes primes = new Primes(10000);
		
	public static void main(String args[])
	{
		int[] E = new int[10000];
		for (int i = 0; i < 10000; i++)
		{
			E[i] = primitude(i);
		}
		
		int[] Ec = Arrays.copyOf(E, E.length);		
		Arrays.sort(Ec);
		
		System.out.println(indexOf(E[9999], Ec));
	}
	
	public static int indexOf(int c, int[] d)
	{
		for (int i = 0; i < d.length; i++)
		{
			if (d[i] == c) return i;
		}
		return -1;
	}
	
	public static int primitude(int n)
	{
		List<Integer> factors = primeFactorisation(n);
		
		int total = 0;
		for (Integer i : factors)
		{
			for (char c : (i + "").toCharArray())
			{
				total += c - '0';
			}
		}
		return total;
	}
	
	public static List<Integer> primeFactorisation(int n)
	{
		ArrayList<Integer> factors = new ArrayList<Integer>();
		for (int i = 0; i < 10000; i++)
		{
			if (primes.isPrime(i))
			{
				if (divides(i, n))
					factors.add(i);
			}
		}
		return factors;
	}
	
	public static boolean divides(int a, int b) 
	{
		return b % a == 0;
	}
}