package library;

/** Immutable representation of a library book. */
public final class Book {
    private final String title;
    private final String author;
    private final String isbn;
    private final int publicationYear;

    public Book(String title, String author, String isbn, int publicationYear) {
        this.title = requireText(title, "Title");
        this.author = requireText(author, "Author");
        this.isbn = requireText(isbn, "ISBN");
        validatePublicationYear(publicationYear);
        this.publicationYear = publicationYear;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getPublicationYear() { return publicationYear; }

    private static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        return value.trim();
    }

    private static void validatePublicationYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        if (year < 1450 || year > currentYear) {
            throw new IllegalArgumentException(
                "Publication year must be between 1450 and " + currentYear + ".");
        }
    }

    @Override
    public String toString() {
        return String.format("ISBN: %s | Title: %s | Author: %s | Year: %d",
                isbn, title, author, publicationYear);
    }
}
