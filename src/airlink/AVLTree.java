package airlink;

import java.util.*;

public class AVLTree<K extends Comparable<K>, V> {
    private class Node {
        K key;
        V value;
        Node left;
        Node right;
        int height;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size;
    private Scanner scanner;

    public AVLTree() {
        root = null;
        size = 0;
        scanner = new Scanner(System.in);
    }

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(Node node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(Node node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    public void insert(K key, V value) {
        root = insertRec(root, key, value);
    }

    private Node insertRec(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRec(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        updateHeight(node);

        int balance = balanceFactor(node);

        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rotateRight(node);
        }

        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return rotateLeft(node);
        }

        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public V search(K key) {
        return searchRec(root, key);
    }

    private V searchRec(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp == 0) {
            return node.value;
        } else if (cmp < 0) {
            return searchRec(node.left, key);
        } else {
            return searchRec(node.right, key);
        }
    }

    public void delete(K key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = deleteRec(node.left, key);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            size--;
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }

            Node minNode = findMin(node.right);
            node.key = minNode.key;
            node.value = minNode.value;
            node.right = deleteRec(node.right, minNode.key);
        }

        if (node == null) {
            return null;
        }

        updateHeight(node);

        int balance = balanceFactor(node);

        if (balance > 1 && balanceFactor(node.left) >= 0) {
            return rotateRight(node);
        }

        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (balance < -1 && balanceFactor(node.right) <= 0) {
            return rotateLeft(node);
        }

        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public List<K> inorder() {
        List<K> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(Node node, List<K> result) {
        if (node != null) {
            inorderRec(node.left, result);
            result.add(node.key);
            inorderRec(node.right, result);
        }
    }

    public List<K> preorder() {
        List<K> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }

    private void preorderRec(Node node, List<K> result) {
        if (node != null) {
            result.add(node.key);
            preorderRec(node.left, result);
            preorderRec(node.right, result);
        }
    }

    public List<K> postorder() {
        List<K> result = new ArrayList<>();
        postorderRec(root, result);
        return result;
    }

    private void postorderRec(Node node, List<K> result) {
        if (node != null) {
            postorderRec(node.left, result);
            postorderRec(node.right, result);
            result.add(node.key);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void printTree() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║              AVL TREE STRUCTURE               ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("  Size: " + size);
        System.out.println("  Height: " + (root != null ? root.height : 0));
        System.out.println("\n  Tree Structure (Root at top):");
        printTreeRec(root, 0, true);
        System.out.println("\n  Legend: (h=X) = height, BF=X = balance factor");
    }

    private void printTreeRec(Node node, int level, boolean isRight) {
        if (node == null) return;
        
        printTreeRec(node.right, level + 1, true);
        
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append("    ");
        }
        if (level > 0) {
            indent.append(isRight ? "┌─── " : "└─── ");
        }
        
        int bf = balanceFactor(node);
        System.out.println(indent.toString() + node.key + " (h=" + node.height + ", bf=" + bf + ")");
        
        printTreeRec(node.left, level + 1, false);
    }

    public void printTreeHorizontal() {
        System.out.println("\n═══════════════════════════════════════════════════");
        System.out.println("              AVL TREE (Horizontal View)            ");
        System.out.println("═══════════════════════════════════════════════════");
        printTreeHorizontalRec(root, 0, true);
        System.out.println("═══════════════════════════════════════════════════");
    }

    private void printTreeHorizontalRec(Node node, int level, boolean isRight) {
        if (node == null) return;
        
        printTreeHorizontalRec(node.right, level + 1, true);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
        if (level > 0) {
            sb.append(isRight ? "↗ " : "↘ ");
        }
        sb.append(node.key);
        if (node.value != null) {
            sb.append(" [" + node.value + "]");
        }
        sb.append(" (BF:" + balanceFactor(node) + ")");
        System.out.println(sb.toString());
        
        printTreeHorizontalRec(node.left, level + 1, false);
    }

    public int getHeight() {
        return root != null ? root.height : 0;
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null) return true;
        int bf = balanceFactor(node);
        return Math.abs(bf) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    public K getMin() {
        if (root == null) return null;
        Node current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.key;
    }

    public K getMax() {
        if (root == null) return null;
        Node current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.key;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public List<V> getAllValues() {
        List<V> result = new ArrayList<>();
        getAllValuesRec(root, result);
        return result;
    }

    private void getAllValuesRec(Node node, List<V> result) {
        if (node != null) {
            getAllValuesRec(node.left, result);
            result.add(node.value);
            getAllValuesRec(node.right, result);
        }
    }

    public Map<Integer, List<K>> getLevelOrder() {
        Map<Integer, List<K>> levels = new HashMap<>();
        levelOrderRec(root, 0, levels);
        return levels;
    }

    private void levelOrderRec(Node node, int level, Map<Integer, List<K>> levels) {
        if (node == null) return;
        levels.computeIfAbsent(level, k -> new ArrayList<>()).add(node.key);
        levelOrderRec(node.left, level + 1, levels);
        levelOrderRec(node.right, level + 1, levels);
    }

    // ==================== DEMONSTRATION METHOD ====================
    
    public void runDemonstration() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           AVL TREE DEMONSTRATION              ║");
        System.out.println("╚════════════════════════════════════════════════╝");

        System.out.println("\n1. Inserting flights into AVL Tree:");
        String[] keys = {"FL001", "FL002", "FL003", "FL004", "FL005"};
        String[] values = {"Flight 1 - Air India", "Flight 2 - IndiGo", "Flight 3 - Vistara", 
                          "Flight 4 - Air India", "Flight 5 - IndiGo"};
        
        for (int i = 0; i < keys.length; i++) {
            insert((K) keys[i], (V) values[i]);
            System.out.println("  Inserted: " + keys[i] + " -> " + values[i]);
        }

        System.out.println("\n2. Tree Statistics:");
        System.out.println("  Size: " + size);
        System.out.println("  Height: " + getHeight());
        System.out.println("  Is Balanced: " + isBalanced());
        System.out.println("  Min Key: " + getMin());
        System.out.println("  Max Key: " + getMax());

        System.out.println("\n3. Tree Traversals:");
        System.out.println("  Inorder: " + inorder());
        System.out.println("  Preorder: " + preorder());
        System.out.println("  Postorder: " + postorder());

        System.out.println("\n4. Level Order (by level):");
        Map<Integer, List<K>> levels = getLevelOrder();
        for (Map.Entry<Integer, List<K>> entry : levels.entrySet()) {
            System.out.println("  Level " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n5. Searching:");
        System.out.println("  Search for FL003: " + search((K) "FL003"));
        System.out.println("  Search for FL006: " + search((K) "FL006"));

        printTree();
        printTreeHorizontal();

        System.out.println("\n6. All Values:");
        System.out.println("  " + getAllValues());
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           DEMONSTRATION COMPLETE               ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    // ==================== INTERACTIVE USER INPUT METHODS ====================
    
    public void showInteractiveMenu() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║           AVL TREE INTERACTIVE MENU                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("  Now you can interact with the AVL Tree!");
        
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║                    MENU OPTIONS                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("  1. Insert a Flight");
            System.out.println("  2. Search for a Flight");
            System.out.println("  3. Delete a Flight");
            System.out.println("  4. Display Tree Structure");
            System.out.println("  5. Display Tree Traversals");
            System.out.println("  6. Display Tree Statistics");
            System.out.println("  7. Display All Flights");
            System.out.println("  8. Exit AVL Tree Demo");
            System.out.println("═══════════════════════════════════════════════════════════");
            System.out.print("  Enter your choice (1-8): ");
            
            int choice = getIntInput();
            switch (choice) {
                case 1: insertFlightInteractive(); break;
                case 2: searchFlightInteractive(); break;
                case 3: deleteFlightInteractive(); break;
                case 4: displayTreeStructure(); break;
                case 5: displayTreeTraversals(); break;
                case 6: displayTreeStatistics(); break;
                case 7: displayAllFlights(); break;
                case 8: 
                    System.out.println("\n✓ Exiting AVL Tree Demo!");
                    return;
                default: System.out.println("✗ Invalid choice. Please try again.");
            }
        }
    }

    private void insertFlightInteractive() {
        System.out.println("\n  === Insert Flight into AVL Tree ===");
        System.out.print("  Enter Flight ID (e.g., FL001): ");
        String key = scanner.nextLine().trim().toUpperCase();
        
        if (search((K) key) != null) {
            System.out.println("  ✗ Flight " + key + " already exists!");
            return;
        }
        
        System.out.print("  Enter Flight Description (e.g., Flight 1 - Air India): ");
        String value = scanner.nextLine().trim();
        
        insert((K) key, (V) value);
        System.out.println("  ✓ Flight " + key + " inserted successfully!");
        System.out.println("  Current Tree Size: " + size);
    }

    private void searchFlightInteractive() {
        System.out.println("\n  === Search for a Flight ===");
        System.out.print("  Enter Flight ID (e.g., FL001): ");
        String key = scanner.nextLine().trim().toUpperCase();
        
        V result = search((K) key);
        if (result != null) {
            System.out.println("  ✓ Flight Found:");
            System.out.println("    Key: " + key);
            System.out.println("    Value: " + result);
        } else {
            System.out.println("  ✗ Flight " + key + " not found!");
        }
    }

    private void deleteFlightInteractive() {
        System.out.println("\n  === Delete a Flight ===");
        System.out.print("  Enter Flight ID (e.g., FL001): ");
        String key = scanner.nextLine().trim().toUpperCase();
        
        if (search((K) key) == null) {
            System.out.println("  ✗ Flight " + key + " not found!");
            return;
        }
        
        delete((K) key);
        System.out.println("  ✓ Flight " + key + " deleted successfully!");
        System.out.println("  Current Tree Size: " + size);
    }

    private void displayTreeStructure() {
        printTree();
        printTreeHorizontal();
    }

    private void displayTreeTraversals() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║              TREE TRAVERSALS                             ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n  Inorder Traversal:");
        System.out.println("    " + inorder());
        
        System.out.println("\n  Preorder Traversal:");
        System.out.println("    " + preorder());
        
        System.out.println("\n  Postorder Traversal:");
        System.out.println("    " + postorder());
        
        System.out.println("\n  Level Order (by level):");
        Map<Integer, List<K>> levels = getLevelOrder();
        for (Map.Entry<Integer, List<K>> entry : levels.entrySet()) {
            System.out.println("    Level " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private void displayTreeStatistics() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║              TREE STATISTICS                             ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        System.out.println("    Size: " + size);
        System.out.println("    Height: " + getHeight());
        System.out.println("    Is Balanced: " + isBalanced());
        System.out.println("    Min Key: " + getMin());
        System.out.println("    Max Key: " + getMax());
    }

    private void displayAllFlights() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║              ALL FLIGHTS IN AVL TREE                     ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        
        if (size == 0) {
            System.out.println("  No flights in the tree.");
            return;
        }
        
        List<K> keys = inorder();
        List<V> values = getAllValues();
        
        System.out.println("\n  " + "-".repeat(60));
        System.out.printf("  %-10s %-40s%n", "Flight ID", "Description");
        System.out.println("  " + "-".repeat(60));
        for (int i = 0; i < keys.size(); i++) {
            System.out.printf("  %-10s %-40s%n", keys.get(i), values.get(i));
        }
        System.out.println("  " + "-".repeat(60));
        System.out.println("  Total: " + size + " flights");
    }

    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("  Please enter a valid number: ");
            }
        }
    }

    // ==================== MAIN METHOD ====================
    
    public static void main(String[] args) {
        AVLTree<String, String> flightMap = new AVLTree<>();
        
        // First run the demonstration
        flightMap.runDemonstration();
        
        // Then show interactive menu
        flightMap.showInteractiveMenu();
        
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           PROGRAM COMPLETED                   ║");
        System.out.println("╚════════════════════════════════════════════════╝");
    }
}