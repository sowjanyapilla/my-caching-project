package com.cache.DocumentCache;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class DetailDocumentCache {
     // Using ConcurrentHashMap to handle cache because it's thread-safe and 
     //suitable for concurrent environments
    private final ConcurrentHashMap<String, Document> cache;

    // The directory where documents will be stored on disk
    private final String diskStoragePath;

    // Maximum number of documents to keep in the cache
    private final int maxCacheSize;

      // AtomicInteger is used to keep track of cache hits in a thread-safe way 
      // without race conditions
      // We use AtomicInteger instead of volatile for the following reasons:
      // 1. Atomic operations: AtomicInteger provides atomic operations like incrementAndGet()
      // 2. Better performance: It's optimized for high contention scenarios
      // 3. More flexibility: It offers additional operations like compareAndSet()
      // 4. Consistency: Ensures consistency in multi-threaded environments
      
    private final AtomicInteger cacheHits = new AtomicInteger(0);
    private final AtomicInteger cacheMisses = new AtomicInteger(0);
    
    public DetailDocumentCache(int maxCacheSize, String diskStoragePath){
        this.maxCacheSize = maxCacheSize;
        this.diskStoragePath = diskStoragePath;
        this.cache = new ConcurrentHashMap<>(maxCacheSize);

  // Create directories if they do not exist for disk storage
        try{
            Files.createDirectories(Paths.get(diskStoragePath));
            System.out.println("Created directories for disk storage at: " + diskStoragePath);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

     // Method to retrieve a document either from cache or from disk storage
      public Document getDocument(String documentId) throws IOException, ClassNotFoundException{
        Document cacheDocument = cache.get(documentId);

         // If the document is found in cache, increment cache hits
         if(cacheDocument != null){
            cacheHits.incrementAndGet();
            return cacheDocument;
         }
         else{
            // If the document is not found, increment cache misses and load from disk
            cacheMisses.incrementAndGet();
            Document document = loadDocumentFromDisk(documentId);
            if(document != null){
                  // Add the document to cache for future access
                  addToCache(documentId, document);

            }
            return document;
        }
      }
         // Method to save a document to both cache and disk storage

         public void saveDocument(Document document) throws IOException{
             // Save the document to the in-memory cache
             cache.put(document.getDocumentId(), document);
             System.out.println("Saved document to cache: " + document.getDocumentId());

             // Evict older documents if cache exceeds the limit
             evictCacheIfNecessary();

               // Save the document to the disk
               saveToDisk(document);
         }


          // Helper method to save a document to disk storage
          private void saveToDisk(Document document) throws IOException{
            Path filePath = Paths.get(diskStoragePath, document.getDocumentId() 
            + ".ser");

              // Use ObjectOutputStream to serialize the document object and write to a file
              try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))){
                out.writeObject(document);
                System.out.println("Saved document to disk: " + filePath);
              }catch (IOException e) {
                System.err.println("Failed to save document to disk: " + filePath);
                e.printStackTrace();
            }
          }

              // Print statistics related to cache performance
            public void printCacheStatistics(){
                System.out.println("Cache Hits: " + cacheHits.get());
                System.out.println("Cache Misses: " + cacheMisses.get());
                System.out.println("Cache Size: " + cache.size());
                System.out.println("Cache Capacity: " + maxCacheSize);
                System.out.println("Cache Efficiency: " + ((double) cacheHits.get() / (cacheHits.get() + cacheMisses.get()) * 100) + "%");
              }

               // Add a document to cache and handle eviction if necessary
               private void addToCache(String documentId, Document document){
                if(cache.size() > maxCacheSize){
                    evictCacheIfNecessary();
                }
                cache.put(documentId, document);
               }

        // Load a document from disk storage
        private Document loadDocumentFromDisk(String documentId) throws IOException,ClassNotFoundException{
            Path filePath = Paths.get(diskStoragePath, documentId + ".ser");
            
             // Check if the file exists on disk and read the document object
             if(Files.exists(filePath)){
                try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(filePath))){
                    return (Document) in.readObject();
                }
             }
             return null;
        } 

         // Evict the oldest document if the cache exceeds the maximum size
         private void evictCacheIfNecessary(){
            while(cache.size() > maxCacheSize){
                String oldestDocument = cache.keySet().iterator().next();
                cache.remove(oldestDocument);
            }
         }

        // Static inner class to represent a Document that implements Serializable
        public static class Document implements Serializable{
            private final String documentId;
            private final String content;
            private final long timestamp;
            private final long lastModifiedTime;

            public Document(String documentId, String content, long timestamp, long lastModifiedTime) {
                this.documentId = documentId;
                this.content = content;
                this.timestamp = timestamp;
                this.lastModifiedTime = lastModifiedTime;
            }

            public String getDocumentId() {
                return documentId;
            }

            public String getContent() {
                return content;
            }

            public long getTimestamp() {
                return timestamp;
            }

            public long getLastModifiedTime() {
                return lastModifiedTime;
            }

            @Override
            public String toString(){
                return "Document{" +
                    "documwntId='" + documentId +'\''+
                    ", content='" + content + '\'' +
                    ", timestamp=" + timestamp +
                    ", lastModifiedTime=" + lastModifiedTime +
                    '}';
            
            }
        
        }

        public static void main(String[] args){
             // Initialize the cache with a max size of 100 and a disk storage path

             DetailDocumentCache cache = new DetailDocumentCache(100,"C:\\Users\\pilla\\Codebase\\my-caching-project\\my-caching-project\\src\\main\\java\\com\\cache\\DocumentCache\\diskStorage");
                                  
             // Using ExecutorService to manage a pool of threads to simulate concurrent access

             ExecutorService executorService = Executors.newFixedThreadPool(8);

             // Simulate multiple threads accessing and modifying the document
             for(int i = 0; i<100; i++){
                final int index = i;
                executorService.execute(() -> {
                    try{
                         // Generate a unique document ID based on the index
                         String id = "Document" + index;
                          // If the index is divisible by 10, create and save a new document
                        if(index % 10 == 0){
                            Document newDocument = new Document(id, "Content for " + id, System.currentTimeMillis(), System.currentTimeMillis());
                            cache.saveDocument(newDocument);
                            System.out.println("Saved document: " + id);
                        }
                             // Otherwise, try to retrieve the document from the cache
                        Document document = cache.getDocument(id);
                             if(document != null){
                                System.out.println("Retrieved document: " + id);
                                
                             }else{
                                System.out.println("Document not found:" + id);;
                        }

                        }catch(IOException | ClassNotFoundException e){
                            e.printStackTrace();
                        }
                });
            
             }
               // Shutdown the ExecutorService and await completion of all tasks
                executorService.shutdown();
                try{
                    executorService.awaitTermination(1, TimeUnit.MINUTES);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }

                //print cache performance statistics
                cache.printCacheStatistics();

        }

}

