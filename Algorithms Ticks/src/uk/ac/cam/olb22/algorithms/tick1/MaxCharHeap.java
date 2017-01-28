package uk.ac.cam.olb22.algorithms.tick1;

import uk.ac.cam.rkh23.Algorithms.Tick1.EmptyHeapException;
import uk.ac.cam.rkh23.Algorithms.Tick1.MaxCharHeapInterface;

import java.util.Arrays;

/**
 * Created by oliver on 28/01/17.
 */
public class MaxCharHeap implements MaxCharHeapInterface {
    private int heapSize = 0;
    private char[] heapArray = new char[1];

    public MaxCharHeap(String s) {
        for (char c : s.toCharArray()) {
            insert(c);
        }
    }

    @Override
    public char getMax() throws EmptyHeapException {
        if (heapSize == 0) {
            throw new EmptyHeapException();
        }
        char oldMax = heapArray[0];
        heapSize--;
        heapArray[0] = heapArray[heapSize];
        if (heapSize > 0) {
            siftDown(0);
        }
        return oldMax;
    }

    private void siftDown(int ki) {
        int kl = 2*ki + 1;
        int kr = 2*ki + 2;
        int km = 0;
        if (kr >= heapSize) {
            if (kl >= heapSize) {
                return;
            } else {
                km = kl;
            }
        } else {
            if (heapArray[kl] >= heapArray[kr]) {
                km = kl;
            } else {
                km = kr;
            }
        }
        if (heapArray[ki] < heapArray[km]) {
            char ty = heapArray[km];
            heapArray[km] = heapArray[ki];
            heapArray[ki] = ty;
            siftDown(km);
        }
    }

    @Override
    public void insert(char c) {
        c = Character.toLowerCase(c);
        if (heapSize >= heapArray.length) {
            heapArray = Arrays.copyOf(heapArray, heapArray.length*2);
        }
        heapArray[heapSize] = c;
        heapSize++;
        shiftUp(heapSize-1);
    }

    private void shiftUp(int ki) {
        if (ki != 0) {
            int kp = (ki-1)/2;
            while (heapArray[kp] < heapArray[ki]) {
                char ty = heapArray[kp];
                heapArray[kp] = heapArray[ki];
                heapArray[ki] = ty;
                shiftUp(kp);
            }
        }
    }

    @Override
    public int getLength() {
        return heapSize;
    }

    public static void main(String[] args) throws EmptyHeapException {
        MaxCharHeap testCharHeap = new MaxCharHeap("123456");
        System.out.println(testCharHeap.heapArray);
        int length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
        testCharHeap.insert('F');
        testCharHeap.insert('G');
        testCharHeap.insert('d');
        System.out.println(testCharHeap.heapArray);
        length = testCharHeap.getLength();
        for (int i = 0; i < length; i++) {
            System.out.println(testCharHeap.getMax());
        }
    }
}
