package library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryManagerTest {
    private LibraryManager manager;
    @BeforeEach void setUp() { manager = new LibraryManager(); }
    private Book book(String title, String isbn) { return new Book(title, "Author", isbn, 2000); }

    @Test void addBookIncreasesInventory() { manager.addBook(book("Dune", "ISBN-1")); assertEquals(1, manager.getAllBooks().size()); assertEquals("Dune", manager.findBookByIsbn("ISBN-1").getTitle()); }
    @Test void rejectsNullBook() { assertThrows(IllegalArgumentException.class, () -> manager.addBook(null)); }
    @Test void rejectsDuplicateIsbnIgnoringCaseAndSpaces() { manager.addBook(book("Dune", "ABC")); assertThrows(IllegalArgumentException.class, () -> manager.addBook(book("Other", " abc "))); assertEquals(1, manager.getAllBooks().size()); }
    @Test void listIsReadOnly() { manager.addBook(book("Dune", "ISBN-1")); assertThrows(UnsupportedOperationException.class, () -> manager.getAllBooks().clear()); }
    @Test void findIsbnIgnoresCaseAndSurroundingSpaces() { manager.addBook(book("Dune", "AbC")); assertNotNull(manager.findBookByIsbn(" abc ")); }
    @Test void findReturnsNullForMissingOrNullIsbn() { assertNull(manager.findBookByIsbn("missing")); assertNull(manager.findBookByIsbn(null)); }
    @Test void updateChangesAllFields() { manager.addBook(book("Old", "ISBN-1")); manager.updateBook("ISBN-1", "New", "New Author", "ISBN-2", 2020); Book b=manager.findBookByIsbn("ISBN-2"); assertEquals("New", b.getTitle()); assertEquals("New Author", b.getAuthor()); assertEquals(2020, b.getPublicationYear()); assertNull(manager.findBookByIsbn("ISBN-1")); }
    @Test void updateCanKeepSameIsbn() { manager.addBook(book("Old", "ISBN-1")); assertDoesNotThrow(() -> manager.updateBook("ISBN-1", "New", "Author", "ISBN-1", 2020)); assertEquals("New", manager.findBookByIsbn("ISBN-1").getTitle()); }
    @Test void updateMissingBookThrows() { assertThrows(IllegalArgumentException.class, () -> manager.updateBook("missing", "New", "Author", "ISBN-2", 2020)); }
    @Test void updateRejectsIsbnOwnedByAnotherBook() { manager.addBook(book("One", "ISBN-1")); manager.addBook(book("Two", "ISBN-2")); assertThrows(IllegalArgumentException.class, () -> manager.updateBook("ISBN-1", "One updated", "Author", "ISBN-2", 2020)); assertEquals("One", manager.findBookByIsbn("ISBN-1").getTitle()); }
    @Test void invalidUpdateDoesNotPartiallyModifyBook() { manager.addBook(book("Original", "ISBN-1")); assertThrows(IllegalArgumentException.class, () -> manager.updateBook("ISBN-1", "Changed", "Author", "ISBN-3", 3000)); assertEquals("Original", manager.findBookByIsbn("ISBN-1").getTitle()); assertNull(manager.findBookByIsbn("ISBN-3")); }
    @Test void deleteExistingBookReturnsTrue() { manager.addBook(book("Dune", "ISBN-1")); assertTrue(manager.deleteBook("ISBN-1")); assertTrue(manager.getAllBooks().isEmpty()); }
    @Test void deleteMissingOrNullIsbnReturnsFalse() { assertFalse(manager.deleteBook("missing")); assertFalse(manager.deleteBook(null)); }

    @Test void returnedInventoryIsSnapshot() {
        manager.addBook(book("First", "ISBN-1"));
        java.util.List<Book> snapshot = manager.getAllBooks();
        manager.addBook(book("Second", "ISBN-2"));
        assertEquals(1, snapshot.size());
        assertEquals(2, manager.getAllBooks().size());
    }

    @Test void updatePreservesInsertionOrder() {
        manager.addBook(book("First", "ISBN-1"));
        manager.addBook(book("Second", "ISBN-2"));
        manager.updateBook("ISBN-1", "First updated", "Author", "ISBN-3", 2020);
        assertEquals("ISBN-3", manager.getAllBooks().get(0).getIsbn());
        assertEquals("ISBN-2", manager.getAllBooks().get(1).getIsbn());
    }

    @Test void isbnLookupIsCaseInsensitiveAfterNormalization() {
        manager.addBook(book("Dune", "AbC-123"));
        assertNotNull(manager.findBookByIsbn("  abc-123  "));
    }
}
