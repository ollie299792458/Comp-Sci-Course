package uk.ac.cam.olb22.algo.English.datatypes.sets;

/**
 * Created by oliver on 03/05/17.
 */
public class RedBlackSet extends AbstractSet {

    @Override
    public void set(int key, Object value) {

    }

    @Override
    public Object get(int key) {
        return null;
    }

    @Override
    public void delete(int key) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean hasKey(int x) {
        return false;
    }

    @Override
    public int chooseAny() {
        return 0;
    }

    @Override
    public int min() {
        return 0;
    }

    @Override
    public int max() {
        return 0;
    }

    @Override
    public int predecessor(int x) {
        return 0;
    }

    @Override
    public int successor(int x) {
        return 0;
    }

    @Override
    public AbstractSet unionWith(AbstractSet s) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    private class Node {
        int k;
        Object x;
        Edge l,r,p;
        boolean b;
        public Node(int k, Object x) {
            this.k =k; this.x = x;
            p = new Edge(null, this);
            l = new Edge(this, null);
            r = new Edge(this, null);
        }
    }

    private class Edge {
        Node t,b;
        public Edge(Node top, Node bottom) {
            this.t = top;
            this.b = bottom;
        }
    }
}
