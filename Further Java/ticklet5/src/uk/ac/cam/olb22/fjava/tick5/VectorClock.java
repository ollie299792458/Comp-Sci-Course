package uk.ac.cam.olb22.fjava.tick5;

import java.util.HashMap;
import java.util.Map;

public class VectorClock {

    private final Map<String, Integer> vectorClock;

    public VectorClock() {
        vectorClock = new HashMap<>();
    }

    public VectorClock(Map<String, Integer> existingClock) {
        vectorClock = new HashMap<>(existingClock);
    }

    public synchronized void updateClock(Map<String,Integer> msgClock) {
        for (Map.Entry<String, Integer> entry : msgClock.entrySet()) {
            //if the string doesn't exist in this clock, or if it's value is smaller than in the other clock
            //then update the value to the value in the recieved clock
            if (vectorClock.get(entry.getKey()) == null
                    || entry.getValue()>vectorClock.get(entry.getKey())) {

                vectorClock.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public synchronized Map<String,Integer> incrementClock(String uid) {
        if (vectorClock.get(uid) == null) {
            vectorClock.put(uid,1);
        } else {
            vectorClock.put(uid, vectorClock.get(uid) + 1);
        }
        return vectorClock;
	    //DONE: why is this required for thread safety?
        //So the message can't change the internals of this vector clock (the return value is passed to the message in line
    }

    public synchronized int getClock(String uid) {
        if (vectorClock.get(uid) == null) {
            return 0;
        } else {
            return vectorClock.get(uid);
        }
    }

    public synchronized boolean happenedBefore(Map<String,Integer> other) {
        //vectorClock happened before other iff all elements of vectorClock are less than or equal to other, and at least
        //one element is strictly smaller
        boolean oneElementStrictlySmaller = false;
        boolean noElementLarger = true;
        boolean concurrent = true;

        for (Map.Entry<String, Integer> entry : vectorClock.entrySet()) {
            //if other has fewer elements it should be false, as the non existant element is 0, therefore less than
            //also if the reverse is true it is valid, as the extra elements in other are 0 in vectorClock, so are
            //less than
            if (other.get(entry.getKey())==null) {
                noElementLarger = false;
                break;
            } else {
                //if they share elements
                concurrent = false;
            }

            if (other.get(entry.getKey())>entry.getValue()) {
                oneElementStrictlySmaller = true;
            }

            if (other.get(entry.getKey())<entry.getValue()) {
                noElementLarger = false;
                break;
            }
        }

        //in this case the other value
        if (other.size()>vectorClock.size()) {
            oneElementStrictlySmaller = true;
        }

        return oneElementStrictlySmaller && noElementLarger;
    }
}