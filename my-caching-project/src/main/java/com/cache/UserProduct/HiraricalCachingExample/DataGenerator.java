package com.cache.UserProduct.HiraricalCachingExample;

import java.util.Random;

public class DataGenerator {

    public static final Random random = new Random();

    public static Product geneProduct(String id){
         return new Product(id, "Product" + id, 10 + random.nextDouble() * 990);
}

public static String generateRandomId(int maxId){
    return "PROD" + random.nextInt(maxId);
}
}
