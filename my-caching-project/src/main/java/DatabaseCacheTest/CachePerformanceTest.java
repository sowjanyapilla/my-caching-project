package DatabaseCacheTest;

import java.util.*;
public class CachePerformanceTest {
    public static void main(String[] args) throws Exception{
        
    }

    public static void testPerformance(int numProducts, int cacheSize)  throws Exception{
        ProductService service = new ProductService(numProducts, cacheSize);
        Random random = new Random();

        System.out.println("\n Testing with " + numProducts + "products and cahe size " +cacheSize);;


        for(int i = 0; i < 1000; i++){
            service.getProduct("PROD" + random.nextInt(numProducts));

        }

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++){
            service.getProduct("PROD" +random.nextInt(numProducts));

        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken for 10,000 random product retreivals: " + (endTime - startTime) + "ms");
        service.printCacheStats();

    }
    
}
