package uk.ac.cam.olb22.own.tpp.twenty19.Question1;

import java.util.*;
import java.util.concurrent.LinkedTransferQueue;

public class Question1Main {
    private static Map<Integer, Boolean> fabulousNumbers = new HashMap<>();

    /*
    How many, biggest

    clarification, leading 0s are allowed
     */


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            fabulousNumbers.put(i, true);
        }

        int count = 0;
        int max = 0;
        for (int i = 10; i < 1000000000; i ++) {
            if (isFabulous(i)) {
                count++;
                max = i;
            }
            if (i % 10000000 == 0) {
                System.out.println(i);
            }
        }

        System.out.println("Final count: " + count);
        System.out.println("Final max: "+ max);
    }

    /*
        Removing a digit divides original, has unique digits, and is fabulous with a digit removed
     */
    public static boolean isFabulous(int x) {
        if (fabulousNumbers.containsKey(x)) {
            return fabulousNumbers.get(x);
        }

        String xs = String.valueOf(x);

        Set<Character> characters = new HashSet<>();

        for (char c : xs.toCharArray()) {
            if (characters.contains(c)) {
                fabulousNumbers.put(x, false);
                return false;
            } else {
                characters.add(c);
            }
        }

        boolean hasDivisor = false;
        for (int i = 0; i < xs.length(); i++) {
            StringBuilder xsb = new StringBuilder(xs);
            int divisor = Integer.parseInt(xsb.deleteCharAt(i).toString());
            if (!(divisor == 0) && (x % divisor == 0)) {
                hasDivisor = true;
                break;
            }
        }
        if (!hasDivisor) {
            fabulousNumbers.put(x, false);
            return false;
        }

        boolean hasFabulous = false;
        for (int i = 0; i < xs.length(); i++) {
            StringBuilder xsb = new StringBuilder(xs);
            if (isFabulous(Integer.parseInt(xsb.deleteCharAt(i).toString()))) {
                hasFabulous = true;
                break;
            }
        }
        if (!hasFabulous) {
            fabulousNumbers.put(x,false);
            return false;
        }

        fabulousNumbers.put(x,true);
        return true;
    }
}
