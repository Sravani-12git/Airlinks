package airlink;

public class SegmentTree {
    private int[] tree;
    private int n;

    public SegmentTree(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];
        build(arr, 0, 0, n - 1);
    }

    private void build(int[] arr, int node, int left, int right) {
        if (left == right) {
            tree[node] = arr[left];
            return;
        }

        int mid = (left + right) / 2;
        build(arr, 2 * node + 1, left, mid);
        build(arr, 2 * node + 2, mid + 1, right);
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    public int query(int qLeft, int qRight) {
        return query(0, 0, n - 1, qLeft, qRight);
    }

    private int query(int node, int left, int right, int qLeft, int qRight) {
        if (qLeft > right || qRight < left) {
            return 0;
        }

        if (qLeft <= left && right <= qRight) {
            return tree[node];
        }

        int mid = (left + right) / 2;
        return query(2 * node + 1, left, mid, qLeft, qRight) +
               query(2 * node + 2, mid + 1, right, qLeft, qRight);
    }

    public void update(int index, int value) {
        update(0, 0, n - 1, index, value);
    }

    private void update(int node, int left, int right, int index, int value) {
        if (left == right) {
            tree[node] = value;
            return;
        }

        int mid = (left + right) / 2;
        if (index <= mid) {
            update(2 * node + 1, left, mid, index, value);
        } else {
            update(2 * node + 2, mid + 1, right, index, value);
        }
        tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
    }

    public static void main(String[] args) {
        // Example: Passenger traffic per hour
        int[] hourlyPassengers = {1800, 1200, 700, 400, 900, 3000, 5000, 6000, 
                                 5500, 4200, 3600, 3000, 2400, 3300, 3800, 
                                 4500, 5000, 5700, 5300, 4200};
        
        SegmentTree segTree = new SegmentTree(hourlyPassengers);
        
        System.out.println("Total passengers from 6:00 to 9:00 (hours 6-9): " + 
                          segTree.query(6, 9));
        System.out.println("Total passengers from 10:00 to 15:00 (hours 10-15): " + 
                          segTree.query(10, 15));
        System.out.println("Total passengers from 17:00 to 19:00 (hours 17-19): " + 
                          segTree.query(17, 19));
        
        // Update passenger count at hour 8
        segTree.update(8, 5800);
        System.out.println("\nAfter updating hour 8 to 5800:");
        System.out.println("Total passengers from 6:00 to 9:00: " + 
                          segTree.query(6, 9));
    }
}