package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Coordinates inventory operations and provides average O(1) ISBN lookup. */
public class LibraryManager {
    // LinkedHashMap provides fast key lookup while retaining insertion order.
    private final Map<String, Book> booksByIsbn = new LinkedHashMap<String, Book>();

    /** Adds a non-null book. ISBN uniqueness is case- and whitespace-insensitive. */
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }
        String key = normalizeIsbn(book.getIsbn());
        if (booksByIsbn.containsKey(key)) {
            throw new IllegalArgumentException("A book with this ISBN already exists.");
        }
        booksByIsbn.put(key, book);
    }

    /** Returns an immutable snapshot so callers cannot change internal inventory. */
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(new ArrayList<Book>(booksByIsbn.values()));
    }

    /** Returns the matching book, or null when the ISBN is absent or null. */
    public Book findBookByIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        return booksByIsbn.get(normalizeIsbn(isbn));
    }

    /** Validates a replacement completely before changing the stored record. */
    public void updateBook(String currentIsbn, String title, String author,
                           String newIsbn, int year) {
        String currentKey = normalizeIsbn(currentIsbn);
        Book existing = booksByIsbn.get(currentKey);
        if (existing == null) {
            throw new IllegalArgumentException("No book found with that ISBN.");
        }

        Book replacement = new Book(title, author, newIsbn, year);
        String newKey = normalizeIsbn(replacement.getIsbn());
        Book isbnOwner = booksByIsbn.get(newKey);
        if (isbnOwner != null && isbnOwner != existing) {
            throw new IllegalArgumentException("Another book already uses this ISBN.");
        }

        // Rebuild only when the key changes; preserve the original list position.
        if (!currentKey.equals(newKey)) {
            Map<String, Book> updated = new LinkedHashMap<String, Book>();
            for (Map.Entry<String, Book> entry : booksByIsbn.entrySet()) {
                if (entry.getKey().equals(currentKey)) {
                    updated.put(newKey, replacement);
                } else {
                    updated.put(entry.getKey(), entry.getValue());
                }
            }
            booksByIsbn.clear();
            booksByIsbn.putAll(updated);
        } else {
            booksByIsbn.put(currentKey, replacement);
        }
    }

    /** Removes a book and reports whether a matching ISBN existed. */
    public boolean deleteBook(String isbn) {
        return isbn != null && booksByIsbn.remove(normalizeIsbn(isbn)) != null;
    }

    private static String normalizeIsbn(String isbn) {
        return isbn.trim().toLowerCase(Locale.ROOT);
    }
}
