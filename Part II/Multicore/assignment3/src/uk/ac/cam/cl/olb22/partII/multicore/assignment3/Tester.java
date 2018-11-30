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

    public static void main(String[] args) {
        //example(16,2);
        readOnly(16,5);
    }
}
