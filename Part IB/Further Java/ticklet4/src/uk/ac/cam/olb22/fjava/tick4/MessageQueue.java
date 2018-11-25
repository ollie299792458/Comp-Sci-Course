package uk.ac.cam.olb22.fjava.tick4;


//A FIFO queue of items of type T
public interface MessageQueue<T> {

    //place msg on back of queue
    void put(T msg);

    //block until queue length > 0; return head of queue
    T take();
}
