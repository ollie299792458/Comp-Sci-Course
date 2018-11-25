package uk.ac.cam.olb22.own.tpp.twenty19;

public class Question5 {
    public Question5()
    {
        System.out.println("Question 5");
        long MAX = Long.MAX_VALUE;
        int count = 1;
        for (long i = 10; i < MAX; i++)
        {
            long sumOfDigits = findSumOfDigits(i);
            long acc = sumOfDigits;
            if (acc != 1) {
                while (acc <= i+1) {
                    if (acc == i) {
                        System.out.println(count+" "+i);
                        count++;
                    }
                    acc *= sumOfDigits;
                }
            }
        }
    }

    private static long findSumOfDigits(long n)
    {
        String s = n + "";
        long total = 0;
        for (char c : s.toCharArray())
        {
            total += c - '0';
        }
        return total;
    }
}
