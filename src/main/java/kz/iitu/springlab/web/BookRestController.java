
package kz.iitu.springlab.web;

import kz.iitu.springlab.catalog.Book;
import kz.iitu.springlab.catalog.BookService;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookRestController {

    private final BookService service;

    public BookRestController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<Book> list(
            @RequestParam(required = false)
            @Size(
                    max = 50,
                    message = "Author search must not exceed 50 characters"
            )
            String author,

            @RequestParam(defaultValue = "10") int limit) {

        return service.findAll(author)
                .stream()
                .limit(limit)
                .toList();
    }

    @GetMapping(value = "/{id}", version = "1.0")
    public ResponseEntity<Book> findV1(@PathVariable long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping(value = "/{id}", version = "2.0")
    public ResponseEntity<BookV2> findV2(@PathVariable long id) {
        Book book = service.getById(id);

        return ResponseEntity.ok(
                new BookV2(
                        book.id(),
                        book.title(),
                        book.author(),
                        book.year(),
                        "2.0"
                )
        );
    }

    public record BookV2(
            Long id,
            String title,
            String author,
            int year,
            String version
    ) {}

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book saved = service.create(book);

        return ResponseEntity
                .created(URI.create("/api/books/" + saved.id()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable long id,
            @RequestBody Book book) {

        Book updated = service.update(id, book);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (!service.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
