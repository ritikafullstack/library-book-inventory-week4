# Library Book Inventory System — Week 4

A refactored Java CLI application for managing library books. The project retains the Week 2 CRUD workflow and builds on the Week 3 JUnit 5 test suite.

## Refactoring highlights
- Replaced repeated linear ISBN scans with a `LinkedHashMap` index for average O(1) lookup, add duplicate checks, and delete-by-ISBN.
- Used `Locale.ROOT` and a single normalization method for consistent case-insensitive ISBN keys.
- Made `Book` immutable so a book cannot be left in a partially updated state.
- Centralized validation in the model and kept inventory rules in `LibraryManager`, separate from console input/output in `Main`.
- `getAllBooks()` now returns an immutable snapshot, protecting internal state and avoiding a live view.
- Preserved insertion order and added regression tests for snapshot and update-order behavior.

## Requirements
- JDK 8 or later
- Maven 3.6 or later

## Run tests
From the folder containing `pom.xml`:

```bash
mvn test
```

## Compile and run
```bash
mvn package
java -cp target/classes library.Main
```

## Project structure
```text
src/main/java/library/   Application source
src/test/java/library/  JUnit 5 tests
pom.xml                 Maven configuration
README.md               Project instructions
```

Performance note: the original `ArrayList` implementation required O(n) ISBN lookup. The refactored `LinkedHashMap` provides average O(1) key lookup, while listing all books remains O(n). No benchmark numbers are claimed; the improvement is based on the data structure's expected complexity.
