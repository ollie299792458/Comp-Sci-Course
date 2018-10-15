package uk.ac.cam.olb22.own.tpp.twenty19;

public class Main {

    public static void main(String[] args) {
        Primes test = new Primes(100);
        System.out.println(test.isPrime(1));
        System.out.println(test.isPrime(5));
        System.out.println(test.isPrime(97));
    }
}
