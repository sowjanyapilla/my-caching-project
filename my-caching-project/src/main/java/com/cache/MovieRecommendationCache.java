
package com.cache;

import java.io.*;
import java.util.*;
public class MovieRecommendationCache {
    /**
     * Cache for storing recent movie recommendations.
     * This cache uses a LinkedHashMap with the following characteristics:
     * 
     * @param initialCapacity 100 - The initial capacity of the map.
     * @param loadFactor 0.75f - The load factor used to determine when to resize the map.
     * @param accessOrder true - Enables access-order iteration (least recently used order).
     * 
     * How it works:
     * 1. The cache stores movie recommendations as key-value pairs (String, String).
     * 2. It has a fixed capacity of 100 entries.
     * 3. When the cache reaches capacity and a new entry is added, the least recently accessed
     *    entry is automatically removed.
     * 4. The load factor of 0.75 means the map will resize when it's 75% full, improving performance.
     * 5. Access-order iteration ensures that entries are ordered based on their access time,
     *    with the least recently used entry at the beginning and the most recently used at the end.
     */
    //L1 Cache Implementation
    // private static final int MAX_ENTRIES = 100;
    // This line declares a constant MAX_ENTRIES with a value of 100.
    // It represents the maximum number of entries that can be stored in the recentMovieCache.
    // The 'static' keyword means this constant is shared across all instances of the class.
    // The 'final' keyword means its value cannot be changed after initialization.

    // This line declares a private final Map named recentMovieCache.
    // It's initialized with a new LinkedHashMap that has specific properties:
    // - Initial capacity of 100 entries
    // - Load factor of 0.75 (75% full before resizing)
    // - Access-order is set to true (entries are ordered by access time)
    // The LinkedHashMap is further customized with an anonymous inner class
    // that overrides the removeEldestEntry method:

    // When access order is set to true in a LinkedHashMap:
    // - The least recently accessed element moves towards the beginning of the map
    // - The most recently accessed element moves towards the end of the map
    //
    // This means:
    // 1. When you access (get or put) an existing entry, it's moved to the end of the map
    // 2. New entries are added at the end of the map
    // 3. The entry at the beginning of the map is considered the "eldest" or least recently used
    //
    // In the context of our cache:
    // - The entry at the beginning (head) of the map is the first candidate for removal
    //   when the cache exceeds its maximum size
    // - The entry at the end (tail) of the map is the most recently used and last to be removed
    //
    // This behavior is ideal for implementing a Least Recently Used (LRU) cache,
    // where we want to keep the most recently accessed items and remove the least recently used.
    // To override a method in the LinkedHashMap object, we can use an anonymous inner class
    // Here's how we can override the removeEldestEntry method:
    private final Map<String, String> recentMovieCache = new LinkedHashMap<String, String>(MAX_ENTRIES, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    // This creates a new LinkedHashMap with the specified initial capacity (MAX_ENTRIES),
    // load factor (0.75f), and access order (true), and immediately overrides the
    // removeEldestEntry method to implement the desired behavior for our cache.

    // The overridden removeEldestEntry method will be called automatically by the
    // LinkedHashMap after each insertion. If it returns true, the map will remove
    // the eldest entry. In this case, it will remove the eldest entry when the size
    // of the map exceeds MAX_ENTRIES.
    // The removeEldestEntry method is crucial for maintaining the cache size, even though
    // LinkedHashMap is growable based on the load factor. Here's why we need it:
    
    // 1. Memory Management:
    //    Without this method, the cache would grow indefinitely, potentially causing
    //    out-of-memory errors in long-running applications.
    
    // 2. Performance:
    //    As the cache grows larger, lookup times can increase, even with efficient
    //    data structures. Keeping the size bounded ensures consistent performance.
    
    // 3. Relevance of Data:
    //    In a movie recommendation system, older entries may become less relevant over time.
    //    Removing the least recently used entries helps keep the cache filled with more
    //    relevant, recent data.
    
    // 4. Customizable Eviction Policy:
    //    This method allows us to implement a custom eviction policy. While we're using
    //    a simple size-based policy here, we could extend this to consider other factors
    //    like entry age or importance.
    
    // 5. Automatic Maintenance:
    //    By overriding this method, we're instructing the LinkedHashMap to automatically
    //    maintain its size after each insertion, without needing external checks or cleanup.
    
    // In summary, while LinkedHashMap can grow based on load factor, the removeEldestEntry
    // method provides a way to bound its growth and implement cache-specific behaviors,
    // which is essential for efficient and effective caching.
    // Removing the load factor from the LinkedHashMap constructor
    // Note: This is not recommended as it may lead to performance issues
    // private final Map<String, String> recentMovieCacheNoLoadFactor = new LinkedHashMap<String, String>(MAX_ENTRIES, true) {
    //     @Override
    //     protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
    //         return size() > MAX_ENTRIES;
    //     }
    // };

    // Explanation:
    // Removing the load factor from the LinkedHashMap constructor is not advisable.
    // The load factor is crucial for controlling the balance between memory usage and performance.
    // Here's why:
    
    // 1. Memory Efficiency:
    //    The load factor determines when the map should be resized. Without it, the map
    //    might resize less efficiently, potentially wasting memory.
    
    // 2. Performance Impact:
    //    A lower load factor generally means better performance but higher memory usage.
    //    Removing it entirely could lead to more collisions and slower operations.
    
    // 3. Default Behavior:
    //    If not specified, Java uses a default load factor of 0.75, which is generally
    //    a good balance between time and space costs.
    
    // 4. Unpredictable Behavior:
    //    Removing the load factor might lead to unpredictable resizing behavior,
    //    potentially affecting the consistency of the cache's performance.
    
    // Instead of removing the load factor, it's better to:
    // - Keep using the removeEldestEntry method to control the cache size
    // - Adjust the initial capacity and load factor based on expected usage patterns
    // - Monitor and profile your application to find the optimal settings
   
    // The L2 cache implementation below is not a method, but a field declaration.
    // It's an instance variable of the MovieRecommendationCache class.
    // This field is initialized with an anonymous subclass of LinkedHashMap
    // that overrides the removeEldestEntry method to implement the LRU eviction policy.
    
    // Here's a breakdown of what this field declaration does:
    // 1. It creates a Map called popularMovieCache with a maximum size of 1000 entries.
    // 2. It uses LinkedHashMap with access-order (true in the constructor) to track LRU (Least Recently Used).
    // 3. It overrides removeEldestEntry to remove the least recently used entry when the size exceeds 1000.
    
    // This implementation allows the cache to automatically manage its size,
    // evicting the least recently used entries when it reaches capacity.
    
    // The removeEldestEntry method is implemented in the LinkedHashMap class.
    // Specifically, it's defined in java.util.LinkedHashMap as a protected method.
    
    // In our code, we're overriding this method in an anonymous subclass of LinkedHashMap.
    // This is done in the popularMovieCache field declaration below.
    
    // Here's a reminder of how it's used:
    //
    // private final Map<String, String> popularMovieCache =
    //     new LinkedHashMap<String, String>(MAX_ENTRIES_L2, 0.75f, true) {
    //         @Override
    //         protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
    //             return size() > MAX_ENTRIES_L2;
    //         }
    //     };
    //
    // By overriding this method, we're customizing the behavior of our LinkedHashMap
    // to automatically remove the oldest entry when the map exceeds a certain size,
    // effectively implementing a Least Recently Used (LRU) cache.

    // While LinkedHashMap is a good choice for implementing an LRU cache,
    // it might not be the best solution for all use cases. Let's consider
    // some alternatives and their trade-offs:

    // 1. Guava Cache:
    // Google's Guava library provides a more feature-rich caching solution.
    // It offers better concurrency, automatic loading of entries, and more
    // flexible eviction policies.

    // Example using Guava Cache:
    // private LoadingCache<String, String> guavaCache = CacheBuilder.newBuilder()
    //     .maximumSize(1000)
    //     .expireAfterWrite(10, TimeUnit.MINUTES)
    //     .build(
    //         new CacheLoader<String, String>() {
    //             @Override
    //             public String load(String key) throws Exception {
    //                 // This method would fetch the value if it's not in the cache
    //                 return fetchMovieFromDatabase(key);
    //             }
    //         });

    // 2. Caffeine:
    // Caffeine is a high-performance, near-optimal caching library.
    // It's inspired by Guava but offers better performance and more features.
    // Yes, both Guava Cache and Caffeine are Java libraries for caching:
    
    // 1. Guava Cache:
    // Guava is a set of core Java libraries from Google, which includes a caching library.
    // To use Guava Cache, you would need to add the Guava dependency to your project.
    // Maven dependency:
    // <dependency>
    //     <groupId>com.google.guava</groupId>
    //     <artifactId>guava</artifactId>
    //     <version>30.1-jre</version>
    // </dependency>
    
    // 2. Caffeine:
    // Caffeine is a high performance, near optimal caching library.
    // To use Caffeine, you would need to add the Caffeine dependency to your project.
    // Maven dependency:
    // <dependency>
    //     <groupId>com.github.ben-manes.caffeine</groupId>
    //     <artifactId>caffeine</artifactId>
    //     <version>3.0.3</version>
    // </dependency>
    
    // Both libraries provide powerful caching capabilities and are widely used in Java applications.
    // The choice between them often depends on specific project requirements and performance needs.

    // // Example using Caffeine:
    // private Cache<String, String> caffeineCache = Caffeine.newBuilder()
    //     .maximumSize(1000)
    //     .expireAfterWrite(10, TimeUnit.MINUTES)
    //     .build();

    // 3. EhCache:
    // EhCache is a widely used, feature-rich caching solution that supports
    // distributed caching and persistence.

    // 4. Redis:
    // For distributed systems, an external cache like Redis might be more appropriate.
    // It allows sharing the cache across multiple instances of your application.

    // The choice depends on your specific requirements:
    // - If you need a simple in-memory cache, LinkedHashMap might suffice.
    // - For better performance and more features, consider Guava or Caffeine.
    // - For distributed caching, consider Redis or a similar solution.

    // // Here's a method that demonstrates using different cache implementations:
    // public String getMovie(String movieName) {
    //     // Using LinkedHashMap (current implementation)
    //     String movie = popularMovieCache.get(movieName);
    //     if (movie == null) {
    //         movie = fetchMovieFromDatabase(movieName);
    //         popularMovieCache.put(movieName, movie);
    //     }

    //     // Using Guava Cache
    //     // try {
    //     //     return guavaCache.get(movieName);
    //     // } catch (ExecutionException e) {
    //     //     // Handle exception
    //     // }

    //     // Using Caffeine
    //     // return caffeineCache.get(movieName, this::fetchMovieFromDatabase);

    //     return movie;
    // }

    // private String fetchMovieFromDatabase(String movieName) {
    //     // Simulate database fetch
    //     return "Fetched " + movieName + " from database";
    // }

    // No, we should not use a normal HashMap for implementing an LRU (Least Recently Used) cache.
    // Here's why:

    // 1. Order: A normal HashMap doesn't maintain any order of its entries.
    //    For an LRU cache, we need to keep track of the order in which elements are accessed.

    // 2. Eviction policy: HashMap doesn't have a built-in mechanism to automatically
    //    remove the least recently used item when the cache reaches its capacity.

    // 3. Access-based reordering: In an LRU cache, we need to move an item to the
    //    "most recently used" position whenever it's accessed. HashMap doesn't support this.

    // LinkedHashMap, on the other hand, is ideal for implementing an LRU cache because:
    // 1. It maintains insertion order by default, or access order when constructed with
    //    the appropriate flag (true for accessOrder in the constructor).
    // 2. It allows us to override the removeEldestEntry method to implement the eviction policy.
    // 3. When using access order, it automatically moves accessed entries to the end of the list.

    // Here's a brief example of how LinkedHashMap is better suited for LRU cache:

    private static final int MAX_ENTRIES = 100;
    private Map<String, String> lruCache = new LinkedHashMap<String, String>(MAX_ENTRIES, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    // This implementation:
    // - Maintains access order (true in constructor)
    // - Automatically evicts the least recently used item when size exceeds MAX_ENTRIES
    // - Moves items to the end of the list (most recently used) when accessed via get() or put()

    // Therefore, LinkedHashMap is the correct choice for implementing an LRU cache in this scenario.

    //L2 Cache Implementation
    private static final int MAX_ENTRIES_L2 = 1000;
    private final Map<String, String> popularMovieCache =
        new LinkedHashMap<String, String>(MAX_ENTRIES_L2, 0.75f, true){
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest){
                return size() > MAX_ENTRIES_L2;
            }
        };
    
    public static void main(String[] args) {
        // LRU Cache for recent movies
        Map<String, String> recentMovieCache = 
          new LinkedHashMap<String, String>(100, 0.75f, true);
        
        // LRU Cache for popular movies
        Map<String, String> popularMovieCache = 
          new LinkedHashMap<String, String>(1000, 0.75f, true);      
          
        String[] genres = {"Action", "Comedy", "Drama", "Horror", "Romance", "Sci-Fi", "Thriller"};
        Random random = new Random();
        
        // Generate 2000 movies with random genres and push into popularMovieCache
        // This for loop iterates 2000 times to create and add movies to the popularMovieCache:
        // 1. It generates a unique movie name ("Movie0", "Movie1", ..., "Movie1999")
        // 2. It randomly selects a genre from the 'genres' array
        // 3. It adds the movie-genre pair to the popularMovieCache using put() method
        // This simulates a database of popular movies with various genres
        for(int i = 0; i < 2000; i++) {
            String movie = "Movie" + i;
            String genre = genres[random.nextInt(genres.length)];
            popularMovieCache.put(movie, genre);
        }
        
        // Generate 100 recently viewed movies and push into recentMovieCache
        for(int i = 0; i < 100; i++) {
            String movie = "Movie" + i;
            recentMovieCache.put(movie, "Recently Viewed");
        }   
        
        // Measure time to fetch a movie from popularMovieCache
        long startTime = System.nanoTime();
        String movie = popularMovieCache.get("Movie100");
        long endTime = System.nanoTime();
        System.out.println("Time taken to fetch the movie: " + (endTime - startTime) + " nanoseconds");
        
        // Optionally print the fetched movie and genre
        System.out.println("Fetched movie: " + movie + ", Genre: " + popularMovieCache.get(movie));
    }
}
