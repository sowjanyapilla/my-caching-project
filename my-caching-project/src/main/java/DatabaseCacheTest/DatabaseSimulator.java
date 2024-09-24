package DatabaseCacheTest;
import java.util.*;

public class DatabaseSimulator {
    private Map<String, Product> products = new HashMap<>();
    private Random random = new Random();

    public DatabaseSimulator(int numProducts){
        for(int i = 0; i < numProducts; i++){
            String id = "PROD" + i;
            products.put(id, new Product(id, "Product " + i, 10 + random.nextDouble() * 90));
        }
        }

        public Product getProduct(String id){
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();

            }

            return products.get(id);
        }

    }

    

