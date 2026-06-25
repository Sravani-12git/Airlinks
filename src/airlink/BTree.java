package airlink;

import java.util.*;

public class BTree<K extends Comparable<K>, V> {
    private static final int MAX_KEYS = 3;
    private static final int MIN_KEYS = 1;

    private class Node {
        List<K> keys;
        List<V> values;
        List<Node> children;
        boolean leaf;

        Node(boolean leaf) {
            this.leaf = leaf;
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
        }
    }

    private Node root;
    private int size;

    public BTree() {
        root = new Node(true);
        size = 0;
    }

    public void insert(K key, V value) {
        if (search(key) != null) {
            return;
        }

        Node r = root;
        if (r.keys.size() == MAX_KEYS) {
            Node s = new Node(false);
            s.children.add(r);
            splitChild(s, 0);
            root = s;
            insertNonFull(s, key, value);
        } else {
            insertNonFull(r, key, value);
        }
        size++;
    }

    private void splitChild(Node parent, int index) {
        Node child = parent.children.get(index);
        Node newChild = new Node(child.leaf);

        int mid = child.keys.size() / 2;

        for (int i = mid + 1; i < child.keys.size(); i++) {
            newChild.keys.add(child.keys.get(i));
            newChild.values.add(child.values.get(i));
        }

        if (!child.leaf) {
            for (int i = mid + 1; i < child.children.size(); i++) {
                newChild.children.add(child.children.get(i));
            }
            for (int i = child.children.size() - 1; i > mid; i--) {
                child.children.remove(i);
            }
        }

        for (int i = child.keys.size() - 1; i >= mid; i--) {
            child.keys.remove(i);
            child.values.remove(i);
        }

        parent.keys.add(index, child.keys.get(mid - 1));
        parent.values.add(index, child.values.get(mid - 1));

        child.keys.remove(mid - 1);
        child.values.remove(mid - 1);

        parent.children.add(index + 1, newChild);
    }

    private void insertNonFull(Node node, K key, V value) {
        if (node.leaf) {
            int i = 0;
            while (i < node.keys.size() && key.compareTo(node.keys.get(i)) > 0) {
                i++;
            }
            node.keys.add(i, key);
            node.values.add(i, value);
        } else {
            int i = 0;
            while (i < node.keys.size() && key.compareTo(node.keys.get(i)) > 0) {
                i++;
            }

            if (node.children.get(i).keys.size() == MAX_KEYS) {
                splitChild(node, i);
                if (key.compareTo(node.keys.get(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key, value);
        }
    }

    public V search(K key) {
        return search(root, key);
    }

    private V search(Node node, K key) {
        if (node == null) {
            return null;
        }

        int i = 0;
        while (i < node.keys.size() && key.compareTo(node.keys.get(i)) > 0) {
            i++;
        }

        if (i < node.keys.size() && key.compareTo(node.keys.get(i)) == 0) {
            return node.values.get(i);
        }

        if (node.leaf) {
            return null;
        }

        return search(node.children.get(i), key);
    }

    public List<K> inorder() {
        List<K> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node node, List<K> result) {
        if (node == null) return;

        int i;
        for (i = 0; i < node.keys.size(); i++) {
            if (!node.leaf && i < node.children.size()) {
                inorder(node.children.get(i), result);
            }
            result.add(node.keys.get(i));
        }
        if (!node.leaf && i < node.children.size()) {
            inorder(node.children.get(i), result);
        }
    }

    public List<V> getAllValues() {
        List<V> result = new ArrayList<>();
        getAllValues(root, result);
        return result;
    }

    private void getAllValues(Node node, List<V> result) {
        if (node == null) return;

        int i;
        for (i = 0; i < node.keys.size(); i++) {
            if (!node.leaf && i < node.children.size()) {
                getAllValues(node.children.get(i), result);
            }
            result.add(node.values.get(i));
        }
        if (!node.leaf && i < node.children.size()) {
            getAllValues(node.children.get(i), result);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = new Node(true);
        size = 0;
    }

    public void printTree() {
        printTree(root, 0);
    }

    private void printTree(Node node, int level) {
        if (node == null) return;

        System.out.print("Level " + level + " [");
        for (int i = 0; i < node.keys.size(); i++) {
            System.out.print(node.keys.get(i));
            if (i < node.keys.size() - 1) System.out.print(", ");
        }
        System.out.print("]");
        if (node.leaf) {
            System.out.print(" (Leaf)");
        }
        System.out.println();

        if (!node.leaf) {
            for (Node child : node.children) {
                printTree(child, level + 1);
            }
        }
    }

    public K findMin() {
        if (root == null || root.keys.isEmpty()) return null;
        Node current = root;
        while (!current.leaf) {
            current = current.children.get(0);
        }
        return current.keys.get(0);
    }

    public K findMax() {
        if (root == null || root.keys.isEmpty()) return null;
        Node current = root;
        while (!current.leaf) {
            current = current.children.get(current.children.size() - 1);
        }
        return current.keys.get(current.keys.size() - 1);
    }
}