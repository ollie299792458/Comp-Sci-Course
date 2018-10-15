package uk.ac.cam.olb22.own.tpp.twenty19;

public class Question7 {
    public Question7() {
        System.out.println("Question 7");
        int LIMIT = 5*(int)Math.pow(10,15);
        int MAX = 400000;
        Primes primes = new Primes(LIMIT);
        int count = 0;
        /*for (long x = 0; x < MAX; x++) {
            long xcub = x*x*x;
            long xqua = xcub*x;
            for (long y = 0; y < x; y++) {
                if (xcub+y*y*y != 0) {
                    long top = (xqua - y * y * y * y);
                    long bottom = (xcub + y * y * y);
                    if (top % bottom == 0) {
                        int p = (int) (top / bottom);
                        if (p > 0) {
                            if (p < LIMIT) {
                                if (primes.isPrime(p)) {
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }*/
        for (int x = 0; x < MAX; x++) {
            long p = 2*x*x+2*x+1;
            if (p > LIMIT) {
                break;
            }
            if (primes.isPrime((int) p)) {
                count++;
                System.out.println(x);
            }
        }

        System.out.println(count);
    }
}
