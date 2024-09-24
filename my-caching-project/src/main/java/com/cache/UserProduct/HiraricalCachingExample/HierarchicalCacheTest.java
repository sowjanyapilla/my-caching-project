package com.cache.UserProduct.HiraricalCachingExample;

import java.io.IOException;

import com.google.common.base.Stopwatch;

public class HierarchicalCacheTest {

    private static final int TOTAL_PRODUCTS = 100000;
    private static final int TEST_ITERATIONS = 1000000;

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        HierarchicalCache cache = new HierarchicalCache(100,1000,"l3cache"); 
        
        System.out.println("Populating cache:");
        for(int i = 0; i < TOTAL_PRODUCTS; i++){
            String id = "PROD" + i;
            cache.put(id, DataGenerator.geneProduct(id));
            
        }

        System.out.println("Testing random access...");

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < TEST_ITERATIONS; i++){

            String randomId = DataGenerator.generateRandomId(TOTAL_PRODUCTS);
            Product product = cache.get(randomId);

            if(product == null){
                System.out.println("Product not found: " + randomId);
            }
            if(i % 100000 == 0){
                System.out.println("Completed " + i + " iterations");
                cache.printStats();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total time for " + TEST_ITERATIONS + "random accesses:" + (endTime - startTime) + "ms");
        cache.printStats();
    }
}
