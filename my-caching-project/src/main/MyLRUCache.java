import java.util.*;
import java.util.Map;
public class MyLRUCache<K,V> extends LinkedHashMap<K,V>{

    private final int capacity;

    public MyLRUCache(int capacity){
        super(capacity, 0.75, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest){
        return size() > capacity;

    }

    public static void main(String[] args){
        MyLRUCache<String, String> cache = new MyLRUCache(3);
        cache.put("key 1", "value 1");
        cache.put("key 2", "value 2");
        cache.put("key 3", "value 3");
        System.out.println(cache);

        cache.get("key 2");
        cache.put("key 4", "value 4");
        cache.put("key 5", "value 5");
        System.out.println(cache);

    }
}