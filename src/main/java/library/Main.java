package library;

import java.util.List;
import java.util.Scanner;

/** Console user interface; inventory rules remain in LibraryManager. */
public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final LibraryManager LIBRARY = new LibraryManager();

    public static void main(String[] args) {
        printHeader();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1: addBook(); break;
                    case 2: listBooks(); break;
                    case 3: updateBook(); break;
                    case 4: deleteBook(); break;
                    case 5: running = false;
                            System.out.println("Thank you for using the Library System!");
                            break;
                    default: System.out.println("Please choose a number from 1 to 5.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
            System.out.println();
        }
    }

    private static void printHeader() {
        System.out.println("==================================");
        System.out.println("   LIBRARY BOOK INVENTORY SYSTEM");
        System.out.println("==================================");
    }

    private static void printMenu() {
        System.out.println("1. Add a book\n2. List all books\n3. Update a book\n4. Delete a book\n5. Exit");
    }

    private static void addBook() {
        System.out.println("--- Add Book ---");
        Book book = new Book(readRequiredText("Title: "), readRequiredText("Author: "),
                readRequiredText("ISBN: "), readInt("Publication year: "));
        LIBRARY.addBook(book);
        System.out.println("Book added successfully.");
    }

    private static void listBooks() {
        List<Book> books = LIBRARY.getAllBooks();
        System.out.println("--- Book Inventory ---");
        if (books.isEmpty()) {
            System.out.println("No books have been added yet.");
            return;
        }
        System.out.println("Total books: " + books.size());
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }

    private static void updateBook() {
        System.out.println("--- Update Book ---");
        String currentIsbn = readRequiredText("Enter the current ISBN: ");
        Book existing = LIBRARY.findBookByIsbn(currentIsbn);
        if (existing == null) {
            System.out.println("No book found with that ISBN.");
            return;
        }
        System.out.println("Current details: " + existing);
        LIBRARY.updateBook(currentIsbn, readRequiredText("New title: "),
                readRequiredText("New author: "), readRequiredText("New ISBN: "),
                readInt("New publication year: "));
        System.out.println("Book updated successfully.");
    }

    private static void deleteBook() {
        String isbn = readRequiredText("Enter the ISBN of the book to delete: ");
        System.out.println(LIBRARY.deleteBook(isbn)
                ? "Book deleted successfully." : "No book found with that ISBN.");
    }

    private static String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty. Please try again.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(INPUT.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}
