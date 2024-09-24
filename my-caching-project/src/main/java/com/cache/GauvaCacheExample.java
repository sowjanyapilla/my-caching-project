// Package declaration for the class
package com.cache;

// Import statements for required Guava cache classes
import com.google.common.cache.*;

// Import statements for Java utility classes
import java.util.Random;
import java.util.concurrent.TimeUnit;

// Main class definition
public class GauvaCacheExample {
    // Main method - entry point of the program
    public static void main(String[] args) {
        // Create a new LoadingCache with specified parameters
        // Create a new LoadingCache using Guava's CacheBuilder
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(100) // Set maximum cache size to 200,000 entries
                .expireAfterWrite(10, TimeUnit.MINUTES) // Set expiration time to 10 minutes after writing
                .build(new CacheLoader<String, String>() { // Build the cache with a CacheLoader
                    @Override
                    public String load(String key) throws Exception {
                        // Define how to load a value if it's not in the cache
                        return "Value for " + key;
                    }
                });
        
        // Explanation:
        // 1. CacheBuilder.newBuilder(): Starts building a new Cache with default settings
        // 2. .maximumSize(200000): Limits the cache to 200,000 entries. When this limit is reached,
        //    the cache will evict entries based on the default eviction policy (likely LRU)
        // 3. .expireAfterWrite(10, TimeUnit.MINUTES): Sets each entry to expire 10 minutes after
        //    it was written, regardless of whether it has been accessed
        // 4. .build(new CacheLoader<>{}): Finalizes the cache build and specifies how to load
        //    new entries when a cache miss occurs
        // 5. The load() method: Defines the logic for creating a new value when a key is not found
        //    in the cache. Here, it simply returns a string "Value for [key]"
        try {
            // Define an array of movie genres
            String[] genres = {"Action", "Comedy", "Drama", "Horror", "Romance", "Sci-Fi", "Thriller"};
            
            // Generate 200,000 movies with random genres and add them to the cache
            for (int i = 0; i < 200000; i++) {
                String movie = "Movie" + i; // Create a movie name
                String genre = genres[new Random().nextInt(genres.length)]; // Select a random genre
                cache.put(movie, genre); // Add the movie and its genre to the cache
            }
            
            // Commented out sleep statement
            //Thread.sleep(1000);
            
            // Perform 100 cache retrievals and measure the time taken
            for (int i = 0; i < 100; i++) {
                long startTime = System.nanoTime(); // Record start time
                String movie = cache.get("Movie9"); // Retrieve a specific movie from cache
                long endTime = System.nanoTime(); // Record end time
                // Print the time taken to fetch the movie
                System.out.println("Time taken to fetch the movie: " + (endTime - startTime) + " nanoseconds");
            }
            
            // Comment indicating that the movie would be retrieved from cache
            // It would be retrieved from cache
        } catch (Exception e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }
}
