package uk.ac.cam.cl.olb22.partII.multicore.assignment3;

import uk.ac.cam.cl.olb22.partII.multicore.assignment3.locks.TestAndTestAndSetLock;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TTSMutex implements Runnable {

    private static int[] ints;
    private int arg = 0;

    public TTSMutex(int x, int loops) {
        this.arg = loops;
        if (ints == null) {
            ints = new int[x];
            for (int i = 0; i < x; i++) {
                ints[i] = i;
            }
        }
    }

    public static String test(int threadCount, int x, int loops) {
        int n = threadCount;
        LocalDateTime start, end;
        Duration duration;
        try {
            List<Thread> threads = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                Runnable r;
                r = new TTSMutex(x, loops);
                Thread t = new Thread(r);
                threads.add(t);
            }
            start = LocalDateTime.now();
            for (Thread t : threads) {
                t.start();
            }
            threads.get(0).join();
            end = LocalDateTime.now();
            for (Thread t : threads) {
                t.join();
            }
            duration = Duration.between(start, end);
        } catch (InterruptedException ie) {
            System.out.println("Caught " + ie);
            duration = Duration.ZERO;
        }
        return duration.toString();
    }

    @Override
    public void run() {
        int sum = sum(arg);
    }

    volatile int temp;
    private int sum(int arg) {
        boolean first = true;
        int sum = 0;
        int oldsum = sum;
        for (int i = 0; i < arg; i++) {
            oldsum = sum;
            TestAndTestAndSetLock.acquireLock();
                sum = 0;
                for (int j = 0; j < ints.length; j++) {
                    sum += ints[j];
                    temp += i + j;
                }
            TestAndTestAndSetLock.releaseLock();
            if (first) {
                oldsum = sum;
                first = false;
            }
            sum = oldsum == sum ? sum : -1;
        }
        return sum;
    }

    public static void main(String args[]) {

        // Start a new thread, and then wait for it to complete:

        System.out.println("How many threads:");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        LocalDateTime start, end;
        Duration duration;
        try {
            List<Thread> threads = new LinkedList<>();
            for (int i = 0; i < n; i++) {
                Runnable r;
                r = new TTSMutex(5, 200);
                Thread t = new Thread(r);
                threads.add(t);
            }
            System.out.println("Starting " + n + " threads");
            start = LocalDateTime.now();
            for (Thread t : threads) {
                t.start();
            }
            threads.get(0).join();
            end = LocalDateTime.now();
            for (Thread t : threads) {
                t.join();
            }
            duration = Duration.between(start, end);
            System.out.println("Joined " + n + " threads");
        } catch (InterruptedException ie) {
            System.out.println("Caught " + ie);
            duration = Duration.ZERO;
        }
        System.out.println("Total time: " + duration.toString());
    }
}