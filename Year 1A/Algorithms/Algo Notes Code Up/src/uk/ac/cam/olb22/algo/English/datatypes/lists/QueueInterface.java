package uk.ac.cam.olb22.algo.English.datatypes.lists;

/**
 * Created by oliver on 03/05/17.
 */
public interface QueueInterface {
    boolean isEmpty();

    void put(Object x);

    Object get();

    Object first();
}
