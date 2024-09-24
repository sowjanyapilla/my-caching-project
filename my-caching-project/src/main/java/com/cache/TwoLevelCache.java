package com.cache;

import java.util.HashMap;
import java.util.Map;

class Product {
    private String name;

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "'}";
    }
}

public class TwoLevelCache {
    private Map<String, Product> l1Cache;
    private Map<String, Product> l2Cache;
    private int L1Capacity;
    private int l2Capacity;
    private int l1Hits;
    private int l2Hits;
    private int misses;

    public TwoLevelCache(int L1Capacity, int l2Capacity) {
        this.l1Cache = new HashMap<>(L1Capacity);
        this.l2Cache = new HashMap<>(l2Capacity);
        this.L1Capacity = L1Capacity;
        this.l2Capacity = l2Capacity;
    }

    public Product get(String key) {
        if (l1Cache.containsKey(key)) {
            l1Hits++;
            return l1Cache.get(key);
        } else if (l2Cache.containsKey(key)) {
            l2Hits++;
            put(key, l2Cache.get(key)); // Fetch from L2 and update L1
            return l2Cache.get(key);
        }
        misses++;
        return null; // Return null if not found
    }

    public void put(String key, Product value) {
        if (l1Cache.size() >= L1Capacity) {
            // Evict the oldest entry from L1 if it's full
            String oldestKey = l1Cache.keySet().iterator().next();
            l1Cache.remove(oldestKey);
        }
        l1Cache.put(key, value);
        l2Cache.putIfAbsent(key, value); // Add to L2 if not present
    }

    public void printStats() {
        System.out.println("L1 Hits: " + l1Hits);
        System.out.println("L2 Hits: " + l2Hits);
        System.out.println("Misses: " + misses);
        System.out.printf("L1 Hit Rate: %.2f%%\n", (l1Hits + l2Hits) > 0 ? (100.0 * l1Hits / (l1Hits + l2Hits + misses)) : 0);
    }

    public static void main(String[] args) {
        // Create a TwoLevelCache instance
        TwoLevelCache cache = new TwoLevelCache(2, 5);

        // Create some Product instances
        Product product1 = new Product("Laptop");
        Product product2 = new Product("Smartphone");
        Product product3 = new Product("Tablet");

        // Add products to the cache
        cache.put("1", product1);
        cache.put("2", product2);
        cache.put("3", product3);

        // Access products
        System.out.println("Accessing Product with key '1': " + cache.get("1"));
        System.out.println("Accessing Product with key '2': " + cache.get("2"));
        System.out.println("Accessing Product with key '3': " + cache.get("3"));
        System.out.println("Accessing Product with key '3': " + cache.get("4"));

        // Check cache statistics
        cache.printStats();

        // Access a product that triggers a L2 hit
        System.out.println("Accessing Product with key '2' again: " + cache.get("2"));

        // Check cache statistics again
        cache.printStats();
    }
}
