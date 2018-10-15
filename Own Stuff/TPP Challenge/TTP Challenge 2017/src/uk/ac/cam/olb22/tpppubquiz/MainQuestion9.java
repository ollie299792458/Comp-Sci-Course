package uk.ac.cam.olb22.tpppubquiz;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class MainQuestion9 {
    public static void main(String[] args) {
        int k = 10000;
        BigInteger[] factorials = new BigInteger[k];
        Map<BigInteger, Integer> vals = new HashMap<>();
        BigInteger last_factorial = BigInteger.ONE;
        factorials[0] = BigInteger.ONE;
        for (int i = 1; i < k; i++) {
            last_factorial = last_factorial.multiply(BigInteger.valueOf(i));
            factorials[i] = last_factorial;
        }
        for (int n = 1; n < k; n++) {
            for (int r = 0; r <= n; r++) {
                BigInteger val = factorials[n].divide(factorials[r].multiply(factorials[n-r]));
                vals.put(val, vals.getOrDefault(val,0) + 1);
                if (vals.get(val) == 5) {
                    System.out.println(val.toString());
                }
            }
        }
    }
}
