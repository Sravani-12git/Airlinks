package airlink;

import java.util.*;

public class SortAlgorithms {
    
    // Merge Sort for sorting flight schedules
    public static void mergeSort(List<Flight> flights, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(flights, left, mid);
            mergeSort(flights, mid + 1, right);
            merge(flights, left, mid, right);
        }
    }

    private static void merge(List<Flight> flights, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        List<Flight> leftList = new ArrayList<>(n1);
        List<Flight> rightList = new ArrayList<>(n2);
        
        for (int i = 0; i < n1; i++) {
            leftList.add(flights.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightList.add(flights.get(mid + 1 + j));
        }
        
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftList.get(i).getDepartureTime().compareTo(rightList.get(j).getDepartureTime()) <= 0) {
                flights.set(k, leftList.get(i));
                i++;
            } else {
                flights.set(k, rightList.get(j));
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            flights.set(k, leftList.get(i));
            i++;
            k++;
        }
        
        while (j < n2) {
            flights.set(k, rightList.get(j));
            j++;
            k++;
        }
    }

    // Quick Sort for ranking flights by delay
    public static void quickSortByDelay(List<Flight> flights, int low, int high) {
        if (low < high) {
            int pi = partitionByDelay(flights, low, high);
            quickSortByDelay(flights, low, pi - 1);
            quickSortByDelay(flights, pi + 1, high);
        }
    }

    private static int partitionByDelay(List<Flight> flights, int low, int high) {
        int pivot = flights.get(high).getDelayMins();
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (flights.get(j).getDelayMins() <= pivot) {
                i++;
                Collections.swap(flights, i, j);
            }
        }
        
        Collections.swap(flights, i + 1, high);
        return i + 1;
    }

    // Heap Sort for finding busiest routes
    public static void heapSortRoutes(List<Route> routes) {
        int n = routes.size();
        
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyRoutes(routes, n, i);
        }
        
        for (int i = n - 1; i > 0; i--) {
            Collections.swap(routes, 0, i);
            heapifyRoutes(routes, i, 0);
        }
    }

    private static void heapifyRoutes(List<Route> routes, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && routes.get(left).getTotalCostUsd() > routes.get(largest).getTotalCostUsd()) {
            largest = left;
        }
        
        if (right < n && routes.get(right).getTotalCostUsd() > routes.get(largest).getTotalCostUsd()) {
            largest = right;
        }
        
        if (largest != i) {
            Collections.swap(routes, i, largest);
            heapifyRoutes(routes, n, largest);
        }
    }

    // Counting Sort for sorting ticket numbers
    public static String[] countingSortTicketNumbers(String[] tickets) {
        // Extract numeric part of ticket numbers
        int[] ticketNums = new int[tickets.length];
        for (int i = 0; i < tickets.length; i++) {
            ticketNums[i] = Integer.parseInt(tickets[i].substring(tickets[i].lastIndexOf('-') + 1));
        }
        
        int max = Arrays.stream(ticketNums).max().orElse(0);
        int min = Arrays.stream(ticketNums).min().orElse(0);
        int range = max - min + 1;
        
        int[] count = new int[range];
        int[] output = new int[tickets.length];
        
        for (int num : ticketNums) {
            count[num - min]++;
        }
        
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }
        
        for (int i = tickets.length - 1; i >= 0; i--) {
            int num = ticketNums[i];
            output[count[num - min] - 1] = i;
            count[num - min]--;
        }
        
        String[] sortedTickets = new String[tickets.length];
        for (int i = 0; i < tickets.length; i++) {
            sortedTickets[i] = tickets[output[i]];
        }
        
        return sortedTickets;
    }

    public static void main(String[] args) {
        // Test sorting algorithms with sample data
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight("FL001", "R001", "AI-101", "Air India", "06:00", "09:00", 
                              "2026-06-25", "On Time", "G1", 200, 150, 130, 0, 1));
        flights.add(new Flight("FL002", "R001", "6E-205", "IndiGo", "08:30", "11:30", 
                              "2026-06-25", "Delayed", "G2", 180, 140, 110, 30, 2));
        flights.add(new Flight("FL008", "R004", "6E-410", "IndiGo", "13:30", "16:45", 
                              "2026-06-25", "Scheduled", "G6", 180, 60, 20, 0, 3));
        
        System.out.println("Flights sorted by departure time (Merge Sort):");
        mergeSort(flights, 0, flights.size() - 1);
        for (Flight f : flights) {
            System.out.println(f.getFlightNumber() + " - " + f.getDepartureTime());
        }
        
        System.out.println("\nFlights sorted by delay (Quick Sort):");
        quickSortByDelay(flights, 0, flights.size() - 1);
        for (Flight f : flights) {
            System.out.println(f.getFlightNumber() + " - Delay: " + f.getDelayMins() + " mins");
        }
        
        // Test ticket sorting
        String[] tickets = {"TKT-2026-005", "TKT-2026-002", "TKT-2026-008", "TKT-2026-001"};
        System.out.println("\nSorted tickets (Counting Sort):");
        String[] sortedTickets = countingSortTicketNumbers(tickets);
        for (String ticket : sortedTickets) {
            System.out.println(ticket);
        }
    }
}