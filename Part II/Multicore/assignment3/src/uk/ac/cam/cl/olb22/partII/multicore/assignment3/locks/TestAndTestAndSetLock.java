package uk.ac.cam.cl.olb22.partII.multicore.assignment3.locks;

public class TestAndTestAndSetLock {
    private volatile static Boolean lock = false;

    public void acquireLock() {
        do {
            while(lock);
        } while (testAndSet());
    }

    public void releaseLock() {
        lock = false;
    }

    private boolean testAndSet() {
        boolean result;
        synchronized (TestAndTestAndSetLock.class) {
            result = lock;
            lock = true;
        }
        return result;
    }
}
