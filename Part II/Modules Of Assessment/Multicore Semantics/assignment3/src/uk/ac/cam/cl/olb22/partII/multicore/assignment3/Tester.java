package uk.ac.cam.cl.olb22.partII.multicore.assignment3;

public class Tester {

    public static void example(int threads, int iterations) {
        for (int i = 1; i <= threads; i++) {
            for (int j =0; j < iterations; j++) {
                System.out.println("threads:"+i+", iteration:"+j+", result:"+Example.test(i));
            }
        }
    }
    public static void readOnly(int threads, int iterations) {
        for (int i = 1; i <= threads; i++) {
            for (int j =0; j < iterations; j++) {
                System.out.println("threads:"+i+", iteration:"+j+", result:"+ReadOnly.test(i));
            }
        }
    }

    private static void ttsMutex(int threads, int iterations) {
        for (int i = 1; i <= threads; i++) {
            //for (int x = 5; x <=5000; x *=10) {
                for (int j = 0; j < iterations; j++) {
                    System.out.println("threads:" + i + ", it:" + j + ", result:" + TTSMutex.test(i,5000,(10000)));
                }
            //}
        }
    }

    private static void ttsRW(int threads, int iterations) {
        for (int i = 1; i <= threads; i++) {
            //for (int x = 5; x <=5000; x *=10) {
            for (int j = 0; j < iterations; j++) {
                System.out.println("threads:" + i + ", it:" + j + ", result:" + TTSRW.test(i,5000,(10000)));
            }
            //}
        }
    }

    public static void main(String[] args) {
        //example(16,2);
        //readOnly(16,5);
        //ttsMutex(16,2);
        ttsRW(16,2);
    }
}
