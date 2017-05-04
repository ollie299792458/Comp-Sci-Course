package uk.ac.cam.olb22.algo.English.datatypes.lists;

/**
 * Created by oliver on 03/05/17.
 */
public class ListDeque implements DequeInterface {

    private Wagon head;
    private Wagon tail;

    @Override
    public boolean isEmpty() {
        return head == null||tail == null;
    }

    @Override
    public void putFront(Object x) {
        Wagon newWagon = new Wagon(x);
        if (isEmpty()) {
            tail = newWagon;
            head = newWagon;
        } else {
            head.last = newWagon;
            newWagon.next = head;
            head = newWagon;
        }
    }

    @Override
    public void putRear(Object x) {
        Wagon newWagon = new Wagon(x);
        if (isEmpty()) {
            tail = newWagon;
            head = newWagon;
        } else {
            tail.next = newWagon;
            newWagon.last = tail;
            tail = newWagon;
        }
    }

    @Override
    public Object getFront() {
        Object result = head.payload;
        head = head.next;
        if (!isEmpty()) {
            head.last = null;
        }
        return result;
    }

    @Override
    public Object getRear() {
        Object result = tail.payload;
        tail = tail.last;
        if (!isEmpty()) {
            tail.next = null;
        }
        return result;
    }

    private class Wagon {
        Object payload;
        Wagon next;
        Wagon last;

        Wagon(Object payload) {
            this.payload = payload;
        }
    }
}
