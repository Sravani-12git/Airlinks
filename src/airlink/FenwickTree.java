package airlink;

public class FenwickTree {
    private int[] bit;
    private int n;

    public FenwickTree(int n) {
        this.n = n;
        bit = new int[n + 1];
    }

    public FenwickTree(int[] arr) {
        this(arr.length);
        for (int i = 0; i < arr.length; i++) {
            update(i + 1, arr[i]);
        }
    }

    public void update(int index, int delta) {
        while (index <= n) {
            bit[index] += delta;
            index += index & -index;
        }
    }

    public int prefixSum(int index) {
        int sum = 0;
        while (index > 0) {
            sum += bit[index];
            index -= index & -index;
        }
        return sum;
    }

    public int rangeSum(int left, int right) {
        if (left > right) {
            return 0;
        }
        return prefixSum(right) - prefixSum(left - 1);
    }

    public static void main(String[] args) {
        // Example: Cumulative passenger counts
        int[] passengerCounts = {1800, 1200, 700, 400, 900, 3000, 5000, 6000, 
                               5500, 4200, 3600, 3000};
        
        FenwickTree fenwick = new FenwickTree(passengerCounts);
        
        System.out.println("Cumulative passengers up to hour 6: " + 
                          fenwick.prefixSum(6));
        System.out.println("Cumulative passengers up to hour 12: " + 
                          fenwick.prefixSum(12));
        System.out.println("Passengers between hour 4 and hour 8: " + 
                          fenwick.rangeSum(4, 8));
        
        System.out.println("\nCumulative totals by hour:");
        for (int i = 1; i <= 12; i++) {
            System.out.println("Hour " + i + ": " + fenwick.prefixSum(i));
        }
    }
}