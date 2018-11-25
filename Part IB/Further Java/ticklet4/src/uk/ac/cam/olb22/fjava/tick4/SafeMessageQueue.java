package uk.ac.cam.olb22.fjava.tick4;

public class SafeMessageQueue<T> implements MessageQueue<T> {
    private static class Link<L> {
        L val;
        Link<L> next;
        Link(L val) {
            this.val = val;
            this.next = null;
        }
    }
    private Link<T> first = null;
    private Link<T> last = null;

    public synchronized void put(T val) {
        Link<T> link = new Link<T>(val);

        assert (first == null && last == null) || (first != null && last != null);

        if (first == null && last == null) {
            last = link;
            first = link;
        } else {
            last.next = link;
            last = link;
        }

        this.notify();
    }

    public synchronized T take() {
        while (first == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                //IGNORED Exception
            }
        }

        Link<T> link = first;

        if (first == last) {
            first = null;
            last = null;
        } else {
            first = first.next;
        }

        return link.val;
    }
}