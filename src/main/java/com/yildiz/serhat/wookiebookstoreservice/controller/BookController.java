package com.yildiz.serhat.wookiebookstoreservice.controller;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.BookCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Book;
import com.yildiz.serhat.wookiebookstoreservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book", description = "Endpoints about books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create/Publish Book")
    public ResponseEntity<?> createBook(@RequestBody @Valid BookCreateRequestDTO request) {
        bookService.createBook(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/author/{id}")
    @Operation(summary = "Get All Book")
    public ResponseEntity<List<Book>> getAllBooks(@PathVariable("id") Long id) {
        return new ResponseEntity<>(bookService.getAllBooksByAuthorId(id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Book By id")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(bookService.getBookById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Book By id")
    public ResponseEntity<?> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unpublish/{id}")
    @Operation(summary = "Unpublish Book By id")
    public ResponseEntity<Book> unpublishBook(@PathVariable("id") Long id) {
        return new ResponseEntity<>(bookService.unpublishBook(id), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Book By id")
    public ResponseEntity<Book> updateBookById(@PathVariable("id") Long id, @RequestBody @Valid BookCreateRequestDTO request) {
        return new ResponseEntity<>(bookService.updateBookById(id, request), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Search Book with criteria")
    public ResponseEntity<List<Book>> searchBook(@RequestParam(value = "description", required = false) String description,
                                                 @RequestParam(value = "author", required = false) Long author,
                                                 @RequestParam(value = "title", required = false) String title) {
        return new ResponseEntity<>(bookService.getBooksWithCriteria(title, description, author), HttpStatus.OK);
    }


}
