package uk.ac.cam.cl.olb22.partII.multicore.assignment3.locks;

public class TestAndTestAndSetLock {
    private volatile static boolean lock = false;

    public static void acquireLock() {
        do {
            //System.out.println("waiting for test");
            while(lock);
            //System.out.println("trying test");
        } while (testAndSet());
        System.out.print("a");
    }

    public static void releaseLock() {
        lock = false;
        System.out.print("r");
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
