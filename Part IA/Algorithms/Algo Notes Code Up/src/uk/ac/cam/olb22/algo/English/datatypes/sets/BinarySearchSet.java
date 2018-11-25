package uk.ac.cam.olb22.algo.English.datatypes.sets;

/**
 * Created by oliver on 03/05/17.
 */
public class BinarySearchSet extends AbstractSet {

    private Node root;

    @Override
    public void set(int key, Object value) {
        Node newNode = new Node(key, value);
        if (isEmpty()) {
            root = newNode;
        } else {
            Node nextNode = root, lastNode = root;
            while (nextNode != null && key != nextNode.key) {
                lastNode = nextNode;
                if (nextNode.key < key) {
                    nextNode = nextNode.rchild;
                } else {
                    nextNode = nextNode.lchild;
                }
            }
            if (nextNode == null) {
                newNode.parent = lastNode;
                if (lastNode.key < newNode.key) {
                    lastNode.rchild = newNode;
                } else {
                    lastNode.rchild = newNode;
                }
            } else {
                nextNode.payload = value;
            }
        }
    }

    @Override
    public Object get(int key) {
        Node node = getNode(key);
        if (node == null) {
            return null;
        } else {
            return node.payload;
        }
    }

    private Node getNode(int key) {
        Node nextNode = root;
        while (nextNode != null && key != nextNode.key) {
            if (nextNode.key < key) {
                nextNode = nextNode.rchild;
            } else {
                nextNode = nextNode.lchild;
            }
        }
        return nextNode;
    }

    @Override
    public void delete(int key) {
        Node toDelete = getNode(key);
        delete(toDelete);
    }

    private void delete(Node toDelete) {
        if (toDelete.lchild==null && toDelete.rchild==null) {
            Node parent = toDelete.parent;
            toDelete.parent = null;
            if (parent.lchild != null && parent.lchild.key == toDelete.key) {
                parent.lchild = null;
            }
            if (parent.rchild != null && parent.rchild.key == toDelete.key) {
                parent.rchild = null;
            }
        } else if (toDelete.lchild == null) {
            Node parent = toDelete.parent;
            if (parent == null) {
                root = toDelete.rchild;
            } else {
                if (toDelete.islChild(parent)) {
                    parent.lchild = toDelete.rchild;
                } else {
                    parent.rchild = toDelete.rchild;
                }
            }
            toDelete.parent = null;
            toDelete.rchild = null;
        } else if (toDelete.rchild == null) {
            Node parent = toDelete.parent;
            if (parent == null) {
                root = toDelete.lchild;
            } else {
                if (toDelete.islChild(parent)) {
                    parent.lchild = toDelete.lchild;
                } else {
                    parent.rchild = toDelete.lchild;
                }
            }
            toDelete.parent = null;
            toDelete.lchild = null;
        } else {
            Node successor = successor(toDelete);
            delete(successor);
            successor.lchild = toDelete.lchild;
            successor.rchild = toDelete.rchild;
            successor.parent = toDelete.parent;
            toDelete.lchild = null;
            toDelete.rchild = null;
            toDelete.parent = null;
            if (toDelete.key == root.key) {
                root = successor;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean hasKey(int x) {
        Node node = getNode(x);
        return node != null;
    }

    @Override
    public int chooseAny() {
        return root.key;
    }

    @Override
    public int min() {
        if (isEmpty()) {
            return Integer.MAX_VALUE;
        }
        Node node = root;
        while (node.lchild != null) {
            node = node.lchild;
        }
        return node.key;
    }

    @Override
    public int max() {
        if (isEmpty()) {
            return Integer.MIN_VALUE;
        }
        Node node = root;
        while (node.rchild != null) {
            node = node.rchild;
        }
        return node.key;
    }

    @Override
    public int predecessor(int x) {
        Node node = getNode(x);
        Node result = predecessor(node);
        if (result == null) {
            return Integer.MAX_VALUE;
        }
        return result.key;
    }

    private Node predecessor(Node node) {
        if (node==null || node.rchild==null) {
            return null;
        }
        if (node.lchild == null) {
            if (node.key == root.key) {
                return null;
            }
            if (node.isrChild(node.parent)) {
                return node.parent;
            }
            node = node.parent;
        }
        node = node.rchild;
        while (node.lchild != null) {
            node = node.lchild;
        }
        return node;
    }

    @Override
    public int successor(int x) {
        Node node = getNode(x);
        Node result = successor(node);
        if (result == null) {
            return Integer.MIN_VALUE;
        }
        return result.key;
    }

    private Node successor(Node node) {
        if (node==null) {
            return null;
        }
        if (node.rchild == null) {
            if (node.key == root.key) {
                return null;
            }
            if (node.islChild(node.parent)) {
                return node.parent;
            }
            node = node.parent;
        }
        node = node.rchild;
        while (node.lchild != null) {
            node = node.lchild;
        }
        return node;
    }

    @Override
    public AbstractSet unionWith(AbstractSet s) {
        if (s instanceof BinarySearchSet) {
            //should flatten and then merge, but dealt with later with other tree types
            while (!s.isEmpty()) {
                int k = s.chooseAny();
                Object x = s.get(k);
                s.delete(k);
                set(k, x);
            }
        } else {
            while (!s.isEmpty()) {
                int k = s.chooseAny();
                Object x = s.get(k);
                s.delete(k);
                set(k, x);
            }
        }
        return this;
    }

    @Override
    public String getName() {
        return "Binary Search Set";
    }

    private class Node {
        int key;
        Object payload;
        Node lchild, rchild, parent;

        Node(int k, Object x) {
            key = k;
            payload = x;
        }

        boolean islChild(Node node) {
            return (node != null)&&(node.lchild!=null)&&(node.key==key)&&(node.lchild.key==key);
        }

        boolean isrChild(Node node) {
            return (node != null)&&(node.rchild!=null)&&(node.key==key)&&(node.rchild.key==key);
        }
    }
}
