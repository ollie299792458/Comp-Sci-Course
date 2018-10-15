package uk.ac.cam.olb22.tpppubquiz;

import java.util.HashSet;
import java.util.Set;

public class MainQuestion8Plus {
    /*PRECONDITIONS: i LUV algos */
    public static void main(String[] args) {
        int k = 1000;
        Set<Integer> set = new HashSet<>();
        for (int a = 1; a < k; a++) {
            for (int e = 1; e < k; e++) {
                for (int c = 1; c < k; c++) {
                    if (a*a+c*c==e*e) {
                        for (int d = 1; d < k; d++) {
                            for (int b = 1; b < k; b++) {
                                if (b*b+a*a==d*d) {
                                    for (int f = 1; f < k; f++) {
                                        if (c*c+b*b==f*f) {
                                            set.add(a*a+b*b+c*c);
                                            for (int g = 1; g < k; g++) {
                                                if ((a*a+b*b+c*c)==g*g) {
                                                    System.out.println("a:"+a+" b:"+b+" c:"+c+" d:"+d+" e:"+e+" f:"+f+" g:"+g);
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(set.toString());
    }
}
