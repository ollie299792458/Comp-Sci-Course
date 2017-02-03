package uk.ac.cam.rkh23.Algorithms.Tick1Star;

import uk.ac.cam.rkh23.Algorithms.Tick1.EmptyHeapException;

public interface MaxHeapInterface< T extends Comparable<T> > {
    // Get the maximum value (or exception if empty)
    public T getMax() throws EmptyHeapException;
    // Insert a new value into the heap (or exception if full)
    public void insert(T i);
    // Get the number of items in the heap
    public int getLength();
}