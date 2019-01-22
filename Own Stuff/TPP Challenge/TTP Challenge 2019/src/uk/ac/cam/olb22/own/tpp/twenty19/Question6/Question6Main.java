package uk.ac.cam.olb22.own.tpp.twenty19.Question6;

import java.util.*;

public class Question6Main {

    public static void main(String[] args) {
        List<List<Integer>> numbers = new LinkedList<>();

        //find xyz
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                for (int z = 0; z < 100; z++) {
                    if (x*x + y*y == z*z) {
                        List<Integer> solution = new ArrayList<>(3);
                        solution.add(x);
                        solution.add(y);
                        solution.add(z);
                        numbers.add(solution);
                    }
                }
            }
        }


        //check abc , digit on front
        for (List<Integer> solution : numbers) {
            for (int i = 1; i < 10; i ++) {
                int a = Integer.parseInt(i+""+solution.get(0));
                int b = Integer.parseInt(i+""+solution.get(1));
                int c = Integer.parseInt(i+""+solution.get(2));
                if ((a*a+b*b==c*c)) {
                    System.out.println(a+" "+b+" "+c);
                }
            }
        }

    }
}

