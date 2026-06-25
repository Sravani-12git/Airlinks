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

    public void delete(K key) {
        if (search(key) == null) {
            return;
        }

        delete(root, key);
        size--;
    }

    private void delete(Node node, K key) {
        if (node == null) {
            return;
        }

        int i = 0;
        while (i < node.keys.size() && key.compareTo(node.keys.get(i)) > 0) {
            i++;
        }

        if (i < node.keys.size() && key.compareTo(node.keys.get(i)) == 0) {
            if (node.leaf) {
                node.keys.remove(i);
                node.values.remove(i);
            } else {
                Node leftChild = node.children.get(i);
                Node rightChild = node.children.get(i + 1);
                if (leftChild.keys.size() >= MIN_KEYS + 1) {
                    K predecessorKey = findPredecessor(leftChild);
                    V predecessorValue = searchValue(leftChild, predecessorKey);
                    node.keys.set(i, predecessorKey);
                    node.values.set(i, predecessorValue);
                    delete(leftChild, predecessorKey);
                } else if (rightChild.keys.size() >= MIN_KEYS + 1) {
                    K successorKey = findSuccessor(rightChild);
                    V successorValue = searchValue(rightChild, successorKey);
                    node.keys.set(i, successorKey);
                    node.values.set(i, successorValue);
                    delete(rightChild, successorKey);
                } else {
                    mergeChildren(node, i);
                    delete(node.children.get(i), key);
                }
            }
            return;
        }

        if (node.leaf) {
            return;
        }

        int childIndex = i;
        if (node.children.get(childIndex).keys.size() == MIN_KEYS) {
            rebalance(node, childIndex);
        }
        delete(node.children.get(childIndex), key);
    }

    private K findPredecessor(Node node) {
        while (!node.leaf) {
            node = node.children.get(node.children.size() - 1);
        }
        return node.keys.get(node.keys.size() - 1);
    }

    private K findSuccessor(Node node) {
        while (!node.leaf) {
            node = node.children.get(0);
        }
        return node.keys.get(0);
    }

    private V searchValue(Node node, K key) {
        for (int i = 0; i < node.keys.size(); i++) {
            if (key.compareTo(node.keys.get(i)) == 0) {
                return node.values.get(i);
            }
        }
        return null;
    }

    private void mergeChildren(Node parent, int index) {
        Node leftChild = parent.children.get(index);
        Node rightChild = parent.children.get(index + 1);
        leftChild.keys.add(parent.keys.remove(index));
        leftChild.values.add(parent.values.remove(index));
        leftChild.keys.addAll(rightChild.keys);
        leftChild.values.addAll(rightChild.values);
        if (!rightChild.leaf) {
            leftChild.children.addAll(rightChild.children);
        }
        parent.children.remove(index + 1);
    }

    private void rebalance(Node parent, int childIndex) {
        Node child = parent.children.get(childIndex);
        if (childIndex > 0) {
            Node leftSibling = parent.children.get(childIndex - 1);
            if (leftSibling.keys.size() > MIN_KEYS) {
                child.keys.add(0, parent.keys.get(childIndex - 1));
                child.values.add(0, parent.values.get(childIndex - 1));
                parent.keys.set(childIndex - 1, leftSibling.keys.remove(leftSibling.keys.size() - 1));
                parent.values.set(childIndex - 1, leftSibling.values.remove(leftSibling.values.size() - 1));
                if (!leftSibling.leaf) {
                    child.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
                }
                return;
            }
        }
        if (childIndex < parent.children.size() - 1) {
            Node rightSibling = parent.children.get(childIndex + 1);
            if (rightSibling.keys.size() > MIN_KEYS) {
                child.keys.add(parent.keys.get(childIndex));
                child.values.add(parent.values.get(childIndex));
                parent.keys.set(childIndex, rightSibling.keys.remove(0));
                parent.values.set(childIndex, rightSibling.values.remove(0));
                if (!rightSibling.leaf) {
                    child.children.add(rightSibling.children.remove(0));
                }
                return;
            }
        }
        if (childIndex > 0) {
            mergeChildren(parent, childIndex - 1);
        } else {
            mergeChildren(parent, childIndex);
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