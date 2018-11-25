package uk.ac.cam.olb22.algo.English.datatypes.priorityqueue.fibonacciheap;

import uk.ac.cam.olb22.algo.English.datatypes.dictionarys.DictionaryInterface;
import uk.ac.cam.olb22.algo.English.datatypes.dictionarys.DirectAddressingDictionary;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by oliver on 10/05/17.
 */
public class FibonacciHeap {

    private FibonacciNode min;

    public FibonacciHeap(){
        min = null;
    }

    public boolean isEmpty() {
        return min == null;
    }

    public FibonacciHeap(FibonacciNode node) {
        node.b = node;
        node.f = node;
        node.p = null;
        node.c = null;
        node.marked = false;
        node.degree = 0;
        min = node;
    }

    public void insert(Object payload, int key) {
        insert(new FibonacciNode(payload,key));
    }

    public void insert(FibonacciNode node) {
        if (node != null) {
            merge(new FibonacciHeap(node));
        }
    }

    public void merge(FibonacciHeap heap) {
        if (heap != null) {
            splice(heap.min);
        }
    }

    public FibonacciNode first() {
        return min;
    }

    public FibonacciNode extractMin() {
        if (min == null) {
            return min;
        }

        if (min.f == min.b) {
            FibonacciNode node = min;
            min = null;
            return node;
        }

        if (min.c != null) {
            min.c.p = null;
            min.c.b.f = min.f;
            min.c.b = min.b;
            min.f.b = min.c.b;
            min.b.f = min.c;

            FibonacciNode nextc = min.c;
            nextc.p = null;
            nextc = nextc.f;
            while (nextc != min.c) {
                nextc.p = null;
            }
        } else {
            min.f.b = min.b;
            min.b.f = min.f;
        }


        FibonacciNode node = min;

        min = min.b;

        mergeTrees();

        findNewMin();

        node.b = node;
        node.f = node;
        node.p = null;
        node.c = null;
        node.marked = false;
        node.degree = 0;
        return node;
    }

    public void decreaseKey(FibonacciNode node, int newKey) {

        node.key = newKey;
        if (node.key < node.p.key) {
            node.marked = true;
            while (node.marked) {
                node.b.f = node.f;
                node.f.b = node.b;
                node.marked = false;
                splice(node);
                node = node.p;
                node.degree--;
            }
            node.marked = true;
        }
    }

    public void delete(FibonacciNode node) {
        decreaseKey(node, Integer.MIN_VALUE);
        extractMin();
    }

    private void mergeTrees() {
        boolean treesMerged = true;
        while (treesMerged) {
            treesMerged = false;
            DictionaryInterface degrees = new DirectAddressingDictionary(1);
            FibonacciNode next = min;
            degrees.set(next.degree, next);
            next = next.f;
            while (next != min) {
                if (degrees.get(next.degree) != null) {
                    treesMerged = true;
                    FibonacciNode toMerge1 = (FibonacciNode) degrees.get(next.degree);
                    FibonacciNode toMerge2 = next;
                    next = next.f;
                    if (toMerge1.key > toMerge2.key) {
                        FibonacciNode temp = toMerge1;
                        toMerge1 = toMerge2;
                        toMerge2 = temp;
                    }

                    toMerge2.f.b = toMerge2.b;
                    toMerge2.b.f = toMerge2.f;

                    if (toMerge1.c != null) {
                        toMerge2.f = toMerge1.c;
                        toMerge2.b = toMerge2.c.b.f;
                    }
                    toMerge1.c = toMerge2;
                    toMerge2.p = toMerge1;
                    degrees.delete(toMerge1.degree);
                    toMerge1.degree++;
                } else {
                    degrees.set(next.degree, next);
                    next = next.f;
                }
            }
        }
    }

    private void findNewMin() {
        FibonacciNode potmin = min.f;
        FibonacciNode newmin = min;
        while (potmin != min) {
            if (newmin.key > potmin.key) {
                newmin = potmin;
            }
            potmin = potmin.f;
        }
        min = newmin;
    }

    private void splice(FibonacciNode node) {
        if (min == null) {
            min = node;
            node.p = null;
            node.f = node;
            node.b = node;
            return;
        }
        FibonacciNode forward = min.f;
        forward.b = node;
        min.f = node;
        node.f = forward;
        node.b = min;
        if (node.key < min.key) {
            min = node;
        }
    }
}
