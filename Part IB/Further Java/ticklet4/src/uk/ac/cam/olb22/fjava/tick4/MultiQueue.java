package uk.ac.cam.olb22.fjava.tick4;

import java.util.HashSet;
import java.util.Set;

public class MultiQueue<T> {

    //Should be final, chose HashSet, ordering not needed
    private final Set<MessageQueue<T>> outputs = new HashSet<MessageQueue<T>>();

    public void register(MessageQueue<T> q) {
        try {
            //editing outputs, so need lock
            synchronized (outputs) {
                //may wish to check return value and pass up
                outputs.add(q);
            }
        } catch (NullPointerException e) {
            System.err.println("MultiQueue: Attempt to deregister null MessageQueue");
        }
    }

    public synchronized void deregister(MessageQueue<T> q) {
        try {
            //editing outputs, so need lock
            synchronized (outputs) {
                //may wish to check return value and pass up
                outputs.remove(q);
            }
        } catch (NullPointerException e) {
            System.err.println("MultiQueue: Attempt to deregister null MessageQueue");
        }
    }

    public void put(T message) {
        try {
            //so message can't be changed halfway through
            synchronized (message) {
                //don't need to synchronize the whole set
                for (MessageQueue<T> output : outputs) {
                    //only need to synchronize individual MessageQueues
                    synchronized (output) {
                        output.put(message);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.err.println("MultiQueue: Attempt to put null T message");
        }
    }
}