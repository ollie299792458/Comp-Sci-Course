package uk.ac.cam.olb22.algorithms.tick1star;

import uk.ac.cam.rkh23.Algorithms.Tick1.EmptyHeapException;
import uk.ac.cam.rkh23.Algorithms.Tick1Star.ComparisonCountingString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 29/01/17.
 */
public class BottomUpMaxHeap<T extends Comparable<T>> extends MaxHeap<T> {
    public BottomUpMaxHeap(List l) {
        super(l);
    }

    @Override
    public T getMax() throws EmptyHeapException {
        if (heap.size() == 0) {
            throw new EmptyHeapException();
        }
        T oldMax = heap.get(0);
        heap.set(0, heap.get(heap.size()-1));
        heap.remove(heap.size()-1);
        if (heap.size() != 0) {
            T newRoot = heap.get(0);
            int lIndex = 0;
            while (heap.size() > getRightChildIndex(lIndex)) {
                if (heap.get(getLeftChildIndex(lIndex)).compareTo(heap.get(getRightChildIndex(lIndex))) >= 0) {
                    lIndex = getLeftChildIndex(lIndex);
                } else {
                    lIndex = getRightChildIndex(lIndex);
                }
            }
            if (heap.size() > getLeftChildIndex(lIndex)) {
                lIndex = getLeftChildIndex(lIndex);
            }

            while (heap.get(lIndex).compareTo(newRoot) < 0) {
                lIndex = getParentIndex(lIndex);
            }

            int p = lIndex;
            T oldtmp = heap.get(lIndex);
            T newtmp;

            while (lIndex != 0) {
                newtmp = heap.get(getParentIndex(lIndex));
                heap.set(getParentIndex(lIndex), oldtmp);
                oldtmp = newtmp;
                lIndex = getParentIndex(lIndex);
            }

            heap.set(p, newRoot);

        }
        return oldMax;
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
        BottomUpMaxHeap<ComparisonCountingString> testCharHeap = new BottomUpMaxHeap<>(list);
        System.out.println(testCharHeap.heap);
        System.out.println(ComparisonCountingString.getComparisonCount());
        int length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
        System.out.println(ComparisonCountingString.getComparisonCount());
    }
}
