package uk.ac.cam.olb22.own.tpp.twenty19;

public class Primes {
    private boolean[] prime;

    public Primes(int n) {
        prime = new boolean[n];

        for (int i = 0; i < n; i++) {
            prime[i] = true;
        }

        prime[0] = false;
        prime[1] = false;

        for (int i = 2; i < Math.sqrt(n); i++) {
            for (int j = i+1; j < n; j++) {
                if (j % i == 0) {
                    prime[j] = false;
                }
            }
        }
    }

    public boolean isPrime(int n) throws PrimesException {
        if (n >= prime.length) {
            throw new PrimesException("Primes out of range");
        }
        return prime[n];
    }

    public class PrimesException extends ArrayIndexOutOfBoundsException {
        public PrimesException(String error) {
            super(error);
        }
    }
}