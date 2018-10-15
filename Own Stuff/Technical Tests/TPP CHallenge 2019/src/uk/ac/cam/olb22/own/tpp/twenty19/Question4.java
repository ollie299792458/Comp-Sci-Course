package uk.ac.cam.olb22.own.tpp.twenty19;

import java.util.ArrayList;
import java.util.List;

public class Question4 {
    public Question4() {
        List<Long> a = new ArrayList<>();
        long sum = 0;
        for(long i= 1; i < Math.pow(10,8); i++){
            long count =i;
            long total = count*count;
            while (total < Math.pow(10,8)) {
                count++;
                total += count*count;
                if (isPalindrome(total)) {
                    if (total < 1000) {
                        System.out.println(total);
                    }
                    if (total < Math.pow(10,8)) {
                        sum += total;
                    }
                }
            }
        }
        System.out.println(sum);
    }

    private static boolean isPalindrome(long n)
    {
        String s = n + "";
        for (int i = 0, j = s.length() - 1; i < j; i++, j--)
        {
            if (s.charAt(i) != s.charAt(j))
                return false;
        }
        return true;
    }
}