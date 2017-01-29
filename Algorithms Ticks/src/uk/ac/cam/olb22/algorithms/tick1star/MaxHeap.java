package uk.ac.cam.olb22.algorithms.tick1star;

import uk.ac.cam.rkh23.Algorithms.Tick1.EmptyHeapException;
import uk.ac.cam.rkh23.Algorithms.Tick1Star.ComparisonCountingString;
import uk.ac.cam.rkh23.Algorithms.Tick1Star.MaxHeapInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oliver on 28/01/17.
 */
public class MaxHeap<T extends Comparable<T>> implements MaxHeapInterface {
    protected List<T> heap = new ArrayList<T>();

    public MaxHeap(List<T> l) {
        for (T o : l) {
            insert(o);
        }
    }

    @Override
    public T getMax() throws EmptyHeapException {
        if (heap.size() == 0) {
            throw new EmptyHeapException();
        }
        T oldMax = heap.get(0);
        heap.set(0, heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        if (heap.size() > 0) {
            siftDown(0);
        }
        return oldMax;
    }

    @Override
    public void insert(Comparable o) {
        heap.add((T) o);
        shiftUp(heap.size()-1);
    }

    private int getParentIndex(int i) {
        return (i-1)/2;
    }

    private int getLeftChildIndex(int i) {
        return 2*i + 1;
    }

    private int getRightChildIndex(int i) {
        return 2*i + 2;
    }

    private void siftDown(int ki) {
        int kl = getLeftChildIndex(ki);
        int kr = getRightChildIndex(ki);
        int km = 0;
        if (kr >= heap.size()) {
            if (kl >= heap.size()) {
                return;
            } else {
                km = kl;
            }
        } else {
            if (heap.get(kl).compareTo(heap.get(kr)) >= 0) {
                km = kl;
            } else {
                km = kr;
            }
        }
        if (heap.get(ki).compareTo(heap.get(km)) < 0) {
            T ty = heap.get(km);
            heap.set(km, heap.get(ki));
            heap.set(ki, ty);
            siftDown(km);
        }
    }

    private void shiftUp(int ki) {
        if (ki != 0) {
            int kp = getParentIndex(ki);
            while (heap.get(kp).compareTo(heap.get(ki)) < 0) {
                T ty = heap.get(kp);
                heap.set(kp, heap.get(ki));
                heap.set(ki, ty);
                shiftUp(kp);
            }
        }
    }

    @Override
    public int getLength() {
        return heap.size();
    }

    public static void main(String[] args) throws EmptyHeapException {
        List<ComparisonCountingString> list = new ArrayList<>();
        list.add(new ComparisonCountingString("a"));
        list.add(new ComparisonCountingString("b"));
        list.add(new ComparisonCountingString("c"));
        list.add(new ComparisonCountingString("d"));
        list.add(new ComparisonCountingString("e"));
        list.add(new ComparisonCountingString("f"));
        list.add(new ComparisonCountingString("g"));
        MaxHeap<ComparisonCountingString> testCharHeap = new MaxHeap<ComparisonCountingString>(list);
        System.out.println(testCharHeap.heap);
        int length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
        System.out.println(ComparisonCountingString.getComparisonCount());
    }
}
