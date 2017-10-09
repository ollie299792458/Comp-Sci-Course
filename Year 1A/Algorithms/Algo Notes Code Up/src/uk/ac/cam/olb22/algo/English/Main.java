package uk.ac.cam.olb22.algo.English;

import uk.ac.cam.olb22.algo.English.datatypes.priorityqueue.fibonacciheap.FibonacciHeap;
import uk.ac.cam.olb22.algo.English.datatypes.priorityqueue.fibonacciheap.FibonacciNode;
import uk.ac.cam.olb22.algo.English.datatypes.sets.AbstractSet;
import uk.ac.cam.olb22.algo.English.datatypes.lists.DequeInterface;
import uk.ac.cam.olb22.algo.English.datatypes.lists.ListDeque;
import uk.ac.cam.olb22.algo.English.datatypes.lists.ListQueue;
import uk.ac.cam.olb22.algo.English.datatypes.lists.QueueInterface;
import uk.ac.cam.olb22.algo.English.datatypes.dictionarys.DictionaryInterface;
import uk.ac.cam.olb22.algo.English.datatypes.dictionarys.DirectAddressingDictionary;
import uk.ac.cam.olb22.algo.English.datatypes.sets.BinarySearchSet;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by oliver on 03/05/17.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Queue test");
        QueueInterface queue = new ListQueue();
        queue.put(new IndexOutOfBoundsException("Hi"));
        queue.put("Hi2");
        System.out.println(queue.first().toString());
        System.out.println(queue.get().toString());
        queue.put(1);
        while (!queue.isEmpty()) {
            System.out.println(queue.get().toString());
        }

        System.out.println();
        System.out.println("Deque test");
        DequeInterface deque = new ListDeque();
        deque.putFront(new IndexOutOfBoundsException("Hi"));
        deque.putRear("Hi2");
        deque.putRear("Hi3");
        System.out.println(deque.getFront().toString());
        System.out.println(deque.getRear().toString());
        deque.putRear(1);
        deque.putRear(2);
        deque.putRear(3);
        deque.putFront(1);
        deque.putFront(2);
        deque.putFront(3);
        while (!deque.isEmpty()) {
            System.out.println(deque.getFront().toString());
            System.out.println(deque.getRear().toString());
        }

        System.out.println();
        System.out.println("Direct addressing dictionarys test");
        DictionaryInterface directAdrDic = new DirectAddressingDictionary(10);
        directAdrDic.set(1, "Hi");
        System.out.println(directAdrDic.get(2));
        System.out.println(directAdrDic.get(1));
        directAdrDic.set(1, "More hi");
        System.out.println(directAdrDic.get(1));
        System.out.println(directAdrDic.get(100));
        directAdrDic.delete(1);
        System.out.println(directAdrDic.get(1));
        directAdrDic.delete(100);
        directAdrDic.set(1000, "Even more hi");
        System.out.println(directAdrDic.get(1000));

        List<AbstractSet> sets = new LinkedList<>();
        sets.add(new BinarySearchSet());
        for (AbstractSet set: sets) {
            testSet(set);
        }

        System.out.println();
        System.out.println("Fibonacci Heap test");
        FibonacciHeap fh = new FibonacciHeap();
        System.out.println(fh.extractMin());
        System.out.println(fh.first());
        System.out.println(fh.isEmpty());
        fh.merge(null);
        fh.insert("Hello", 0);
        System.out.println(fh.isEmpty());
        System.out.println(fh.first().getPayLoad().toString());
        System.out.println(fh.extractMin().getPayLoad().toString());
        fh.insert("Hello2", -1);
        fh.insert("Hello", 0);
        fh.insert("Hello3", -4);
        System.out.println(fh.extractMin().getPayLoad().toString());
        fh.merge(new FibonacciHeap(new FibonacciNode("Hello4", -100)));
        System.out.println(fh.extractMin().getPayLoad().toString());
    }

    private static void testSet(AbstractSet set) {
        System.out.println();
        System.out.println(set.getName());
        set.set(1, "HI");
        set.set(1, "hi");
        System.out.println(set.get(set.chooseAny()).toString());
        set.set(2, "Chicken");
        System.out.println(set.hasKey(2));
        System.out.println(set.hasKey(3));
        System.out.println(set.max());
        System.out.println(set.min());
        System.out.println(set.successor(1));
        System.out.println(set.predecessor(1));
        set.delete(1);
        System.out.println(set.get(set.chooseAny()).toString());
    }
}
