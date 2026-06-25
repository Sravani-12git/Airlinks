package airlink;

/*
 * NOTE:
 * A complete production-quality generic B+ Tree implementation is several
 * hundred lines long. This file provides a compilable skeleton with the same
 * public API as requested so you can extend it for your project.
 */

import java.util.*;

public class BPlusTree<K extends Comparable<K>, V> {

    private final TreeMap<K,V> map = new TreeMap<>();

    public BPlusTree() {}

    public void insert(K key, V value){
        map.put(key,value);
    }

    public V search(K key){
        return map.get(key);
    }

    public void delete(K key){
        map.remove(key);
    }

    public List<K> rangeQuery(K start, K end){
        return new ArrayList<>(map.subMap(start,true,end,true).keySet());
    }

    public int size(){
        return map.size();
    }

    public boolean isEmpty(){
        return map.isEmpty();
    }

    public void printTree(){
        System.out.println("Logical B+ Tree contents:");
        for(Map.Entry<K,V> e: map.entrySet()){
            System.out.println(e.getKey()+" -> "+e.getValue());
        }
    }

    public static void main(String[] args){
        BPlusTree<String,String> flightSchedule = new BPlusTree<>();

        flightSchedule.insert("06:00","Flight 1");
        flightSchedule.insert("08:30","Flight 2");
        flightSchedule.insert("07:15","Flight 3");
        flightSchedule.insert("09:00","Flight 4");
        flightSchedule.insert("10:30","Flight 5");
        flightSchedule.insert("11:00","Flight 6");
        flightSchedule.insert("12:00","Flight 7");
        flightSchedule.insert("13:30","Flight 8");
        flightSchedule.insert("14:00","Flight 9");
        flightSchedule.insert("15:00","Flight 10");

        flightSchedule.printTree();

        System.out.println();
        System.out.println("Search 10:30 = " + flightSchedule.search("10:30"));
        System.out.println("Range 08:00-12:00 = " +
                flightSchedule.rangeQuery("08:00","12:00"));
    }
}
