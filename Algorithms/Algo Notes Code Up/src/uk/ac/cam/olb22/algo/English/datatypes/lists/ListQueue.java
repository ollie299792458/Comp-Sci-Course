package uk.ac.cam.olb22.algo.English.datatypes.lists;

/**
 * Created by oliver on 03/05/17.
 */
public class ListQueue implements QueueInterface{

    private Wagon head;

    private Wagon tail;

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void put(Object x) {
        Wagon newWagon = new Wagon(x);
        if (isEmpty()) {
            head = newWagon;
            tail = newWagon;
        } else {
            tail.next = newWagon;
            tail = newWagon;
        }

    }

    @Override
    public Object get() {
        Object result = head.payload;
        head = head.next;
        return result;
    }

    @Override
    public Object first() {
        return head.payload;
    }

    private class Wagon {
        Object payload;
        Wagon next;

        Wagon(Object payload) {
            this.payload = payload;
        }
    }
}
