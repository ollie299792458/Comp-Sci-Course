package uk.ac.cam.cl.olb22.partII.multicore.assignment3.locks;

import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAndTestAndSetLock {
    private volatile static AtomicBoolean lock = new AtomicBoolean();

    private volatile static int temp;

    public static void acquireLock() {
        do {
            //System.out.println("waiting for test");
            while(lock.get());
            //System.out.println("trying test");
        } while (testAndSet());
        //temp += LocalDate.now().hashCode();
    }

    public static void releaseLock() {
        lock.set(false);
        //temp += LocalDate.now().hashCode();
    }

    private static boolean testAndSet() {
        boolean result;
        //System.out.println("waiting for set");
        result = lock.getAndSet(true);
        return result;
    }
}
