package uk.ac.cam.olb22.own.tpp.twenty19;

import java.util.LinkedList;
import java.util.List;

public class Question1 {
    public Question1() {
        long total = 0;
        long last = 1;
        long lastlast = 1;
        for (long i = 0; last < 4000000; i++) {
            long next = last + lastlast;
            if (next % 2 == 0) {
                total += next;
            }
            lastlast = last;
            last = next;
        }
        System.out.println(total);
    }
}
