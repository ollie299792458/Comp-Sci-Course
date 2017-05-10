package uk.ac.cam.olb22.algo.English.datatypes.priorityqueue.fibonacciheap;

/**
 * Created by oliver on 10/05/17.
 */
public class FibonacciNode {
    private Object payLoad;
    int key;
    FibonacciNode b,f,p,c;
    boolean marked = false;
    int degree = 0;

    public FibonacciNode(Object payLoad, int key) {
        this.payLoad = payLoad;
        this.key = key;
    }

    public Object getPayLoad() {
        return payLoad;
    }
}
