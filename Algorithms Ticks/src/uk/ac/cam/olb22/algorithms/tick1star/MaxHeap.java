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
            System.out.println(heap);
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
        heap.add(heap.size(), (T) o);
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

    private void swap (int i1, int i2) {
        T temp = heap.get(i1);
        heap.set(i1, heap.get(i2));
        heap.set(i2, temp);
    }

    private void siftDown(int ki) {
        int kl = getLeftChildIndex(ki);
        int kr = getRightChildIndex(ki);
        int km = 0;
        if (kr >= heap.size()) {
            if (!(kl >= heap.size())) {
                km = kl;
                if (heap.get(ki).compareTo(heap.get(km)) < 0) {
                    swap(km, ki);
                }
            }
        } else {
            if (heap.get(kl).compareTo(heap.get(kr)) >= 0) {
                km = kl;
            } else {
                km = kr;
            }
            if (heap.get(ki).compareTo(heap.get(km)) < 0) {
                swap(km,ki);
                siftDown(km);
            }
        }
    }

    private void shiftUp(int ki) {
        if (ki != 0) {
            int kp = getParentIndex(ki);
            while (heap.get(kp).compareTo(heap.get(ki)) < 0) {
                swap(ki,kp);
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
        list.add(new ComparisonCountingString("c"));
        list.add(new ComparisonCountingString("d"));
        list.add(new ComparisonCountingString("a"));
        list.add(new ComparisonCountingString("r"));
        list.add(new ComparisonCountingString("g"));
        list.add(new ComparisonCountingString("s"));
        list.add(new ComparisonCountingString("w"));
        list.add(new ComparisonCountingString("h"));
        list.add(new ComparisonCountingString("k"));

        /*list.add(new ComparisonCountingString("a"));
        list.add(new ComparisonCountingString("z"));
        list.add(new ComparisonCountingString("b"));
        list.add(new ComparisonCountingString("y"));
        list.add(new ComparisonCountingString("c"));
        list.add(new ComparisonCountingString("x"));
        list.add(new ComparisonCountingString("d"));
        list.add(new ComparisonCountingString("w"));
        list.add(new ComparisonCountingString("e"));
        list.add(new ComparisonCountingString("v"));
        list.add(new ComparisonCountingString("f"));
        list.add(new ComparisonCountingString("u"));
        list.add(new ComparisonCountingString("g"));
        list.add(new ComparisonCountingString("t"));
        list.add(new ComparisonCountingString("h"));
        list.add(new ComparisonCountingString("s"));
        list.add(new ComparisonCountingString("i"));
        list.add(new ComparisonCountingString("r"));
        list.add(new ComparisonCountingString("j"));
        list.add(new ComparisonCountingString("q"));
        list.add(new ComparisonCountingString("k"));
        list.add(new ComparisonCountingString("p"));
        list.add(new ComparisonCountingString("l"));
        list.add(new ComparisonCountingString("o"));
        list.add(new ComparisonCountingString("m"));
        list.add(new ComparisonCountingString("n"));*/
        MaxHeap<ComparisonCountingString> testCharHeap = new MaxHeap<ComparisonCountingString>(list);
        System.out.println(testCharHeap.heap);
        System.out.println(ComparisonCountingString.getComparisonCount());
        for (ComparisonCountingString string : testCharHeap.heap) {
            System.out.print(string + " " + string.getCompCount()+" ");
        }
        System.out.println();
        int length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
        System.out.println(ComparisonCountingString.getComparisonCount());
    }
}
