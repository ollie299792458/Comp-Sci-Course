package uk.ac.cam.olb22.algo.supo2;

public class Fibonacci {

    private static int callCount = 0;

    public static int fibonacci(int n) {
        callCount++;
        if (n == 0 || n == 1) {
            return 1;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    public static void main(String[] args) {
        fibonacci(10);
        System.out.println(callCount);
        callCount=0;
        fibonacci(20);
        System.out.println(callCount);
        callCount=0;
        fibonacci(30);
        System.out.println(callCount);
    }
}
