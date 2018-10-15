package uk.ac.cam.olb22.own.tpp.twenty19;

import java.util.ArrayList;
import java.util.List;

public class Question2 {
    public Question2() {
        //generate increasing and decreasing
        int MAX = 4000000;
        char[] bouncy = new char[MAX];
        final char INC = 1;
        final char BOU = 0;
        final char DEC = 2;
        for (int i = 0; i < bouncy.length; i++) {
            String s = i +"";
            if (decreasing(s)) {
                bouncy[i] = DEC;
            } else if (increasing(s)) {
                bouncy[i] = INC;
            } else {
                bouncy[i] = BOU;
            }
        }

        int count = 0;

        for (int i = 0; i < MAX; i++) {
           if (bouncy[i] == BOU) {
               count++;
           }
           if (((double) count )/((double) i ) >= 0.9) {
               System.out.println(i);
               break;
           }
        }
    }

    private boolean increasing(String s) {
        if (s.length() < 2) {
            return true;
        }
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(i-1)) {
                return false;
            }
        }
        return true;
    }

    private boolean decreasing(String s) {
        if (s.length() < 2) {
            return true;
        }
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) > s.charAt(i-1)) {
                return false;
            }
        }
        return true;
    }
}
