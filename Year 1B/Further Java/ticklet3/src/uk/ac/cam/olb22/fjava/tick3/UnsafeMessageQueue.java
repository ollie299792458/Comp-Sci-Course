package uk.ac.cam.olb22.fjava.tick3;

public class UnsafeMessageQueue<T> implements MessageQueue<T> {
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

    public void put(T val) {
    	Link<T> link = new Link<T>(val);

    	assert (first == null && last == null) || (first != null && last != null);

    	if (first == null && last == null) {
    	    last = link;
    	    first = link;
        } else {
            last.next = link;
            last = link;
        }
    }

    public T take() {
        while(first == null) { //use a loop to block thread until data is available
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                // Ignored exception
                // What causes this exception to be thrown? and what should
                //       you do with it ideally?
                // From the docs: "InterruptedException - if any thread has interrupted the current thread. The
                // interrupted status of the current thread is cleared when this exception is thrown."
                // Answer: The thread has been interrupted somehow. So we should ignore it as our implementation should
                // make no attempt to be thread safe.
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