package library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test void trimsTextFields() { Book b = new Book("  Dune ", " Frank Herbert ", "  ISBN-1  ", 1965); assertEquals("Dune", b.getTitle()); assertEquals("Frank Herbert", b.getAuthor()); assertEquals("ISBN-1", b.getIsbn()); }
    @Test void rejectsNullOrBlankTitle() { assertThrows(IllegalArgumentException.class, () -> new Book(" ", "Author", "X1", 2000)); assertThrows(IllegalArgumentException.class, () -> new Book(null, "Author", "X1", 2000)); }
    @Test void rejectsBlankAuthorAndIsbn() { assertThrows(IllegalArgumentException.class, () -> new Book("Title", " ", "X1", 2000)); assertThrows(IllegalArgumentException.class, () -> new Book("Title", "Author", " ", 2000)); }
    @Test void acceptsValidPublicationYear() { assertDoesNotThrow(() -> new Book("Title", "Author", "X1", 2000)); }
    @Test void rejectsPublicationYearOutsideAllowedRange() { int current = java.time.Year.now().getValue(); assertThrows(IllegalArgumentException.class, () -> new Book("Title", "Author", "X1", 1449)); assertThrows(IllegalArgumentException.class, () -> new Book("Title", "Author", "X1", current + 1)); }
}
