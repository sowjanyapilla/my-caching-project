package DatabaseCacheTest;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.*;

public class ProductService {
    private final DatabaseSimulator database;
    private final LoadingCache<String, Product> cache;


    public ProductService(int numProducts, int cacheSize){

        this.database = new DatabaseSimulator(numProducts);
        
        this.cache = CacheBuilder.newBuilder()
        .maximumSize(cacheSize)
        .expireAfterWrite(10,TimeUnit.MINUTES)
        .recordStats()
        .build(new CacheLoader<String, Product>() {
            
            @Override
            public Product load(String id){
                return database.getProduct(id);
            }

            
        });
    }

        public Product getProduct(String id) throws Exception{
            return cache.get(id);
        }

        public void printCacheStats(){
            System.out.println("Cache stats: " + cache.stats());
        }
    }
    
