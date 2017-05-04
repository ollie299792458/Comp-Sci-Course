package uk.ac.cam.olb22.algo.English.datatypes.lists;

/**
 * Created by oliver on 03/05/17.
 */
public interface DequeInterface{
    boolean isEmpty();
    void putFront(Object x);
    void putRear(Object x);
    Object getFront();
    Object getRear();
}
