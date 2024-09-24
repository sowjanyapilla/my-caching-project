package com.cache.WeatherDataCachingSystem;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

// WeatherData class implementing Serializable with transient fields
class WeatherData implements Serializable{
    private static final long serialVersionUID = 1L;

    private String condition;
    private double temperature;
    private transient double humidity;  // Not saved during serialization

    public WeatherData(String condition, double temperature, double humidity) {
        this.condition = condition;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Override
    public String toString(){
        return "Condition: " + condition + ", Temperature: " + temperature + "°C";
    } 
}

// Cache class using ConcurrentHashMap and AtomicInteger for cache size management

class WeatherCache{
    private final ConcurrentHashMap<String, WeatherData> cache = new ConcurrentHashMap<>();
    private final AtomicInteger cacheSize = new AtomicInteger(0);
    private final int MAX_CACHE_SIZE = 5;

    public WeatherData getWeatherData(String location){
        return cache.get(location);

    }

    public void addWeatherData(String location, WeatherData data){
        if(cacheSize.get() < MAX_CACHE_SIZE){
            cache.put(location, data);
            cacheSize.incrementAndGet();
        }else{
            System.out.println("Cache full, not adding new data.");
        }
    }

    public boolean isCached(String location){
        return cache.containsKey(location);
    }

    public void clearCache(){
        cache.clear();
        cacheSize.set(0);
    }
}

// WeatherService class simulating API fetch, caching, and handling multi-threaded requests
class WeatherService{
    private final WeatherCache cache = new WeatherCache();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
     // Method to retrieve weather data, either from cache or simulated API
     public Future<WeatherData> getWeather(final String location){
        return executor.submit(() -> {
            if(cache.isCached(location)){
                System.out.println("Fetching from cache for location: " + location);
                return cache.getWeatherData(location);
            }else{
                System.out.println("Fetching from API for location: " + location);
                WeatherData data = fetchWeatherFromAPI(location);
                    cache.addWeatherData(location, data);
                    return data;
                }   
        });
     }
    // Simulated API call to fetch weather data
    private WeatherData fetchWeatherFromAPI(String location) throws InterruptedException{
        Thread.sleep(1000);
        return new WeatherData("Sunny", Math.random() * 35, Math.random() * 100); // Random weather data

    }

    public void shutdownService(){
        executor.shutdown();
    }

}

public class WeatherCachingSystem{
    public static void main(String[] args) throws Exception{
        WeatherService service = new WeatherService();

        
        try {

         // Multiple threads fetching weather data using Future and ExecutorService
         Future<WeatherData> future1 = service.getWeather("London");
         Future<WeatherData> future2 = service.getWeather("Paris");
         Future<WeatherData> future3 = service.getWeather("New York");
         Future<WeatherData> future4 = service.getWeather("Tokyo");
         Future<WeatherData> future5 = service.getWeather("Berlin");
         Future<WeatherData> future6 = service.getWeather("London"); // Cached
 
         // Fetch and print results
         System.out.println("Weather in London: " + future1.get());
         System.out.println("Weather in Paris: " + future2.get());
         System.out.println("Weather in New York: " + future3.get());
         System.out.println("Weather in Tokyo: " + future4.get());
         System.out.println("Weather in Berlin: " + future5.get());
         System.out.println("Weather in London (cached): " + future6.get());
        }catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            service.shutdownService();
    
    }
}
 }
