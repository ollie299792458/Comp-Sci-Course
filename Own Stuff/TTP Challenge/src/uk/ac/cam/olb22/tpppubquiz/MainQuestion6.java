package uk.ac.cam.olb22.tpppubquiz;

public class MainQuestion6 {
    public static int k = 1000;
    public static void main(String[] args) {
        for (int a = 0; a < k; a++) {
            for (int b = 0; b < k; b++) {
                for (int c = 0; c < k; c++) {
                    if (Math.pow(a,b)*Math.pow(c,a) == a*1000+b*100+c*10+a) {
                        System.out.println("a:"+a+" b:"+b+" c:"+c);
                        return;
                    }
                }
            }
        }
    }
}
