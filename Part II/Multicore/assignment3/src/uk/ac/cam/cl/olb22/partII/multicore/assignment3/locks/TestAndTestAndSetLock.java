package uk.ac.cam.cl.olb22.partII.multicore.assignment3.locks;

import java.time.LocalDate;
import java.util.Date;

public class TestAndTestAndSetLock {
    private volatile static boolean lock = false;

    private volatile static int temp;

    public static void acquireLock() {
        do {
            //System.out.println("waiting for test");
            while(lock);
            //System.out.println("trying test");
        } while (testAndSet());
        temp += LocalDate.now().hashCode();
    }

    public static void releaseLock() {
        lock = false;
        temp += LocalDate.now().hashCode();
    }

    private static boolean testAndSet() {
        boolean result;
        //System.out.println("waiting for set");
        synchronized (TestAndTestAndSetLock.class) {
            //System.out.println("setting");
            result = lock;
            lock = true;
        }
        return result;
    }
}
