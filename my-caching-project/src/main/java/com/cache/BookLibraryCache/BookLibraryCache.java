package com.cache.BookLibraryCache;

import java.awt.print.Book;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookLibraryCache {
    private final Map<String, Book> bookDatabase = new HashMap<>();
    //Our cache
    private final Map<String, Book> cache = new HashMap<>();
    

    
    private final int CACHE_SIZE = 5;
     private int cacheHits = 0;
     private int cacheMisses = 0;
      
    public BookLibraryCache(){
        bookDatabase.put("1",new Book("1","Book1","Author1"));
        bookDatabase.put("2",new Book("2","Book2","Author2"));
        bookDatabase.put("3",new Book("3","Book3","Author3"));
        bookDatabase.put("4",new Book("4","Book4","Author4"));
        bookDatabase.put("5",new Book("5","Book5","Author5"));
        bookDatabase.put("6",new Book("6","Book6","Author6"));
        
     }

     public Book getBook(String bookId){
        Book book = cache.get(bookId);
        if(book!=null){
            cacheHits++;
            System.out.println("Cache Hit for bookId: " + bookId);
            return book;
        }
        else{
            cacheMisses++;
            System.out.println("Cache Miss for bookId:"  + bookId);
            
            book = bookDatabase.get(bookId);
            if(book != null){
                addToCache(bookId, book);
            }
            return book;
        }
        }

        private void addToCache(String bookId, Book book){
            if(cache.size() > CACHE_SIZE){
                String keyTORemove = cache.keySet().iterator().next();
                cache.remove(keyTORemove);
                System.out.println("Cache is full. Removing lest recently used bookId: " +keyTORemove);;
            }
            cache.put(bookId, book);

        }

            public void printCacheStatistics(){
                System.out.println("Cach Hits: " +cacheHits);
                System.out.println("Ccahe Misses: " +cacheMisses);;
                System.out.println("Cache Hit Ratio: " + ((double) cacheHits /(cacheHits + cacheMisses)));
                System.out.println("Current Ccahe size: " +cache.size());
                System.out.println("Books in Cache: " + cache.keySet());

            }

            private static class Book {
                private String id;
                private String title;
                private String author;

                public Book(String id, String title, String author){
                        this.id = id;
                        this.title = title;
                        this.author = author;
                }

                @Override
                public String toString(){
                    return "Book{id = '"+id+"', title = '"+title+"', author = '"+author+"'}";
                }
                
            }
        

            public static void main(String[] args){
                BookLibraryCache cache=new BookLibraryCache();
                String []requestedBooks={"1","2","3","4","5","6","1","2","3","4","5","6","5","4","3","9"};
                for(String bookId:requestedBooks){
                    
                    Book book=cache.getBook(bookId);
                    if(book!=null){
                        System.out.println("retived book: "+book);
                    }
                    else{
                        System.out.println("Book not found : "+book);
                    }
                    System.out.println("-----------------------------------");
        
                }
                cache.printCacheStatistics();
            }

        }
        
   
