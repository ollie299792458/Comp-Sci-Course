package uk.ac.cam.olb22.tpppubquiz;

import java.util.ArrayList;
import java.util.List;

public class MainQuestion4 {

    public static List<Integer> primes = new ArrayList<Integer>();
    public static int k = 1000;

    public static boolean isPrime(int n) {
        for(int i = 2; i < n; i++){
            if(n % i == 0){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int last_factorial = 1;
        List<Integer> ans = new ArrayList<Integer>();
        for(int i = 1; i < k; i++){
            last_factorial *= i;
            if(isPrime(last_factorial + 1)){
                System.out.println(i+", ");
            }
        }
    }
}
