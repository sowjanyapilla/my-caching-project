// Package declaration for the UserProduct module
package com.cache.UserProduct;

// Importing necessary Java utilities
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Definition of the Product class
class Product {
    // Private fields to store product details
    private String id;      // Unique identifier for the product
    private String name;    // Name of the product
    private double price;   // Price of the product

    // Constructor to initialize a Product object
    public Product(String id, String name, double price) {
        this.id = id;       // Set the product ID
        this.name = name;   // Set the product name
        this.price = price; // Set the product price
    }

    // Getters and setters are not implemented here, but would typically be included

    // Override the toString method to provide a string representation of the Product
    @Override
    public String toString() {
        // Return a formatted string containing all product details
        return "Product{id='" + id + "', name='" + name + "', price=" + price + "}";
    }
}

// Definition of the DatabaseSimulator class to mimic a product database
class DatabaseSimulator {
    // Map to store products, with product ID as key and Product object as value
    private Map<String, Product> products = new HashMap<>();
    // Random object for generating random prices
    private Random random = new Random();

    // Constructor to initialize the DatabaseSimulator with a specified number of products
    public DatabaseSimulator(int numProducts) {
        // Loop to create and add the specified number of products
        for (int i = 0; i < numProducts; i++) {
            // Generate a product ID
            String id = "PROD" + i;
            // Create a new Product with the ID, a name, and a random price between 10 and 100
            products.put(id, new Product(id, "Product " + i, 10 + random.nextDouble() * 90));
        }
    }

    // Method to retrieve a product by its ID
    public Product getProduct(String id) {
        // Simulate database access delay
        try {
            Thread.sleep(100); // 100ms delay to simulate DB access
        } catch (InterruptedException e) {
            // Print stack trace if sleep is interrupted
            e.printStackTrace();
        }
        // Return the product from the map using the provided ID
        return products.get(id);
    }
}
