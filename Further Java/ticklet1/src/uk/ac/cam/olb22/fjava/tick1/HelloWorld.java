package uk.ac.cam.olb22.fjava.tick1;

public class HelloWorld {
    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println("Hello, "+args[0]);
        } else {
            System.out.println("Hello, world");
        }
    }
}
