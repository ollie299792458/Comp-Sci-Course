package uk.ac.cam.olb22.tpppubquiz;

public class MainQuestion11 {
    public static void main(String[] args) {
        int k = 73939133;
        boolean[] notprime = new boolean[k];
        notprime[0] = true;
        notprime[1] = true;
        for (int i = 2; i < k; i++) {
            if (!notprime[i]) {
                for (int c = i*2; c < k; c = c+i) {
                    notprime[c] = true;
                }
            }
        }
        int count = 0;
        for (int i = k-1; i >= 0; i--) {
            if (!notprime[i]) {
                int tocheck = i;
                boolean primealldaway = true;
                while (tocheck > 10) {
                    tocheck = tocheck / 10;
                    if (notprime[tocheck]) {
                        primealldaway = false;
                        break;
                    }
                }
                if (primealldaway) {
                    count++;
                }
            }
        }
        System.out.println(count);
    }
}
