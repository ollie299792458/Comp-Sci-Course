package uk.ac.cam.cl.olb22.partII.multicore.assignment3;
// To compile:
//
// javac Example.java

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.*;

class Example implements Runnable {

  // Delay function waits a variable time controlled by "d".  The function
  // writes to a per-object volatile field -- this aims to prevent the compiler
  // from optimizing the delay away completely.

  volatile int temp;
  void delay(int arg) {
    for (int i = 0; i < arg; i++) {
      for (int j = 0; j < 1000000; j++) {
	    this.temp += i + j;
      }
    }
  }

  // Constructor for an "Example" object.  Fields in the object can be
  // used to pass values to/from the thread when it is started and
  // when it finishes.

  int arg;
  int result;

  Example(int arg) {
    this.arg = arg;
  }

  // Example thread function.  This just delays for a little while,
  // controlled by the parameter passed when the thread is started.

  public void run() {
    System.out.println("Thread started arg=" + arg);
    delay(arg);
    result = 42;
    System.out.println("Thread done result=" + result);
  }

  // Shared variable for use with example atomic compare and swap
  // operations (ai.compareAndSet in this example).

  static AtomicInteger ai = new AtomicInteger(0);

  // Main function

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
        Example e1 = new Example(100);
        Thread t = new Thread(e1);
        threads.add(t);
      }
      System.out.println("Starting "+n+" threads");
      start = LocalDateTime.now();
      for (Thread t : threads) {
        t.start();
      }
      for (Thread t : threads) {
        t.join();
      }
      end = LocalDateTime.now();
      duration = Duration.between(start,end);
      System.out.println("Joined "+n+" threads");
    } catch (InterruptedException ie) {
      System.out.println("Caught " + ie);
      duration = Duration.ZERO;
    }
    System.out.println("Total time: "+duration.toString());

    // Example compare and swap operations

    boolean s;
    System.out.println("ai=" + ai);
    s = ai.compareAndSet(0, 1);
    System.out.println("ai=" + ai + " s=" + s);
    s = ai.compareAndSet(0, 2);
    System.out.println("ai=" + ai + " s=" + s);
    s = ai.compareAndSet(1, 2);
    System.out.println("ai=" + ai + " s=" + s);
  }
}
