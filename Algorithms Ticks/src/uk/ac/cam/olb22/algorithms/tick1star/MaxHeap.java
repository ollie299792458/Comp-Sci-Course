package uk.ac.cam.olb22.algorithms.tick1star;

import uk.ac.cam.rkh23.Algorithms.Tick1.EmptyHeapException;
import uk.ac.cam.rkh23.Algorithms.Tick1Star.MaxHeapInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oliver on 28/01/17.
 */
public class MaxHeap<T extends Comparable<T>> implements MaxHeapInterface {
    private List<T> heap = new ArrayList<T>();

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
    public void insert(T o) {
        heap.add(o);
        shiftUp(heap.size()-1);
    }

    private void siftDown(int ki) {
        int kl = 2*ki + 1;
        int kr = 2*ki + 2;
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
            int kp = (ki-1)/2;
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
        MaxHeap<Character> testCharHeap = new MaxHeap(Arrays.asList("123456".toCharArray()));
        System.out.println(testCharHeap.heap);
        int length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
        testCharHeap.insert('F');
        testCharHeap.insert('G');
        testCharHeap.insert('d');
        System.out.println(testCharHeap.heap);
        length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
    }
}
