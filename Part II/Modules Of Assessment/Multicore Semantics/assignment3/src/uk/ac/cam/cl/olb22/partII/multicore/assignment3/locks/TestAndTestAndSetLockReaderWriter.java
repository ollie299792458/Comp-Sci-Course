package uk.ac.cam.cl.olb22.partII.multicore.assignment3.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAndTestAndSetLockReaderWriter {
    private volatile static AtomicInteger lock = new AtomicInteger();

    private volatile static int temp;

    public static void acquireWrite() {
        do {
            if (lock.get() == 0 && lock.compareAndSet(0,-1)) break;
        } while (true);
    }

    public static void releaseWrite() {
        lock.set(0);
    }

    public static void acquireRead() {
        do {
            int oldVal = lock.get();
            if (oldVal >= 0 && lock.compareAndSet(oldVal,oldVal+1)) break;
        } while (true);
    }

    public static void releaseRead() {
        lock.decrementAndGet();
    }
}
