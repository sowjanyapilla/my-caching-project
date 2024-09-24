package com.cache.BookLibraryCache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;

public class BookLibrarySystem {
    public static void main(String[] args){
        BookLibrary bookLibrary = new BookLibrary();
        
        bookLibrary.addBook(new BookLibrary.Book("123","Harry Potter","J.K.Rowling"));
        bookLibrary.addBook(new BookLibrary.Book("456","To Kill a Mockingbird","Harper Lee"));
        bookLibrary.addBook(new BookLibrary.Book("789","1984","George Orwell"));
        bookLibrary.addBook(new BookLibrary.Book("101","Pride and Prejudice","Jane Austen"));
        bookLibrary.addBook(new BookLibrary.Book("102","The Great Gatsby","F. Scott Fitzgerald"));
        bookLibrary.addBook(new BookLibrary.Book("103","Moby Dick","Herman Melville"));
        bookLibrary.addBook(new BookLibrary.Book("104","War and Peace","Leo Tolstoy"));
        bookLibrary.addBook(new BookLibrary.Book("105","Hamlet","William Shakespeare"));
        bookLibrary.addBook(new BookLibrary.Book("106","Macbeth","William Shakespeare"));

        System.out.println(bookLibrary.getAllBooks());
        System.out.println(bookLibrary.getAuthors());
        System.out.println(bookLibrary.getBookCountByAuthor());

        Future<BookLibrary.Book> futureMostPopularBook = bookLibrary.getMostPopularBookAsync();
        try{
            BookLibrary.Book mostPopularBook = futureMostPopularBook.get();
            System.out.println("Most Popular book: " + mostPopularBook.toString());
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        
        }finally{
            bookLibrary.executorService.shutdown();
        }

        System.out.println("Program continues while waiting for the most popular book");
    }

    static class BookLibrary{
        private List<Book> books = new ArrayList<>();
        private Set<String> authors = new HashSet<>();
        private Map<String, Integer> bookCountByAuthor = new HashMap<>();
        private ExecutorService executorService = Executors.newSingleThreadExecutor();
        
        BookLibrary(){
            books = new ArrayList<>();
            authors = new HashSet<>();
            bookCountByAuthor = new HashMap<>();
            executorService = Executors.newSingleThreadExecutor();
        }
        
        public void addBook(Book book){
            books.add(book);
            authors.add(book.author);
            bookCountByAuthor.put(book.author, bookCountByAuthor.getOrDefault(book.author, 0) + 1);
        }

        public List<Book> getAllBooks(){
            return new ArrayList<>(books);
        }

        public Set<String> getAuthors(){
            return new HashSet<>(authors);
        }

        public Map<String, Integer> getBookCountByAuthor(){
            return new HashMap<>(bookCountByAuthor);
        }

        public Future<Book> getMostPopularBookAsync(){
            return executorService.submit(() -> {
                Thread.sleep(2000);
                return books.isEmpty() ? null : getMostPopularBook();
            });
        }

        private Book getMostPopularBook(){
            return books.get(0);
        }

        static class Book implements Serializable{
            private String isbn;
            private String title;
            private String author;
            transient int currentPage = 0;

            public Book(String isbn, String title, String author){
                this.isbn = isbn;
                this.title = title;
                this.author = author;
            }

            @Override
            public String toString(){
                return "Book{" + "isbn='" + isbn + '\'' + ", title='" + title + '\'' + ", author='" + author + '\'' + '}';
            }
        }
    }
}