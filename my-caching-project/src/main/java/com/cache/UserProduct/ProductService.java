// Package declaration for the UserProduct module
package com.cache.UserProduct;

// Import statements for Guava cache classes and Java concurrent utilities
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

// Definition of the ProductService class
public class ProductService {
    // Declaration of a final DatabaseSimulator instance
    private final DatabaseSimulator database;
    // Declaration of a final LoadingCache instance for caching Product objects
    private final LoadingCache<String, Product> cache;

    // Constructor for ProductService, taking number of products and cache size as parameters
    public ProductService(int numProducts, int cacheSize) {
        // Initialize the database simulator with the specified number of products
        this.database = new DatabaseSimulator(numProducts);
        // Initialize the cache using Guava's CacheBuilder
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(cacheSize) // Set the maximum number of entries the cache may contain
            .expireAfterWrite(10, TimeUnit.MINUTES) // Set entries to expire 10 minutes after creation
            .recordStats() // Enable statistics collection for the cache
            .build(new CacheLoader<String, Product>() { // Build the cache with a custom CacheLoader
                @Override
                public Product load(String id) {
                    // Define how to load a product into the cache if it's not present
                    return database.getProduct(id);
                }
            });
    }

    // Method to retrieve a product by its ID
    public Product getProduct(String id) throws Exception {
        // Attempt to get the product from the cache, loading it from the database if necessary
        return cache.get(id);
    }

    // Method to print the current cache statistics
    public void printCacheStats() {
        // Print the cache statistics to the console
        System.out.println("Cache stats: " + cache.stats());
    }
}
