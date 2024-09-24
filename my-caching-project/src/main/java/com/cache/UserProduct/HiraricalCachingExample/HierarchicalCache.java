package com.cache.UserProduct.HiraricalCachingExample;

import com.google.common.cache.*;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class HierarchicalCache {
    private final Cache<String, Product> l1Cache;
    private final Cache<String, Product> l2Cache;
    private final Path l3CcaheDir;

    public HierarchicalCache(int l1Size, int l2Size, String l3Path) throws IOException{
        this.l1Cache = CacheBuilder.newBuilder()
        .maximumSize(l1Size)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .recordStats()
        .build();

        this.l2Cache = CacheBuilder.newBuilder()
        .maximumSize(l2Size)
        .expireAfterWrite(5,TimeUnit.MINUTES)
        .recordStats()
        .build();

        this.l3CcaheDir = Paths.get(l3Path);
        Files.createDirectories(l3CcaheDir);

    }

    public Product get(String key) throws IOException, ClassNotFoundException{
        Product product = l1Cache.getIfPresent(key);
        if (product != null) {
            return product;
        }
        product = l2Cache.getIfPresent(key);
        if(product != null){
            l1Cache.put(key, product);
            return product;
        }

        Path filePath = l3CcaheDir.resolve(key);
        if(Files.exists(filePath)){
            try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))){
                product = (Product) ois.readObject();
                l2Cache.put(key, product);
                l1Cache.put(key, product);
                return product;
            }
        }
        return null;
    }

        public void put(String key, Product value) throws IOException{
            l1Cache.put(key, value);
            l2Cache.put(key, value);

            Path filePath = l3CcaheDir.resolve(key);
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))){
                oos.writeObject(value);
            }
        }

        public void printStats(){
            System.out.println("L1 Cache State: "+ l1Cache.stats());
            System.out.println("L2 Cache Stats: " + l2Cache.stats());
        }
    }