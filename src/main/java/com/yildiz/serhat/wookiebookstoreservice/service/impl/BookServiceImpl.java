package com.yildiz.serhat.wookiebookstoreservice.service.impl;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.BookCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Book;
import com.yildiz.serhat.wookiebookstoreservice.exception.BookNotFoundException;
import com.yildiz.serhat.wookiebookstoreservice.repository.BookRepository;
import com.yildiz.serhat.wookiebookstoreservice.service.AuthorService;
import com.yildiz.serhat.wookiebookstoreservice.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorService authorService;

    @Override
    public void createBook(BookCreateRequestDTO request) {
        Book book = Book.buildBookFromRequest(request);
        Author author = null;
        if (!request.authorId().isEmpty()) {
            author = authorService.getAuthorById(Long.valueOf(request.authorId()));
        }
        book.setAuthor(author);
        Book savedBook = repository.save(book);
        log.info("Book saved with id:{}", savedBook.getId());
    }

    @Override
    public List<Book> getAllBooksByAuthorId(Long id) {
        Author author = authorService.getAuthorById(id);
        List<Book> authorsBook = repository.findByAuthor(author);
        if (authorsBook.isEmpty()) {
            String message = String.format("Book Not found for this author id:%s Not Found", id);
            log.error(message);
            throw new BookNotFoundException(message);
        }
        return authorsBook;
    }

    @Override
    public Book getBookById(Long id) {
        return getBook(id);
    }

    @Override
    public Book unpublishBook(Long id) {
        Book book = getBook(id);
        book.setActive(false);
        log.info("Book is unpublished with Id: {}", book.getId());
        return repository.save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        Book book = getBook(id);
        repository.deleteById(book.getId());
        log.info("Book deleted with Id: {}", book.getId());
    }

    @Override
    public Book updateBookById(Long id, BookCreateRequestDTO request) {
        Book book = getBook(id);
        Book updatedBook = Book.updateBook(book, request);
        log.info("Book is updated with Id: {}", updatedBook.getId());
        return repository.save(updatedBook);
    }

    @Override
    public List<Book> getBooksWithCriteria(String title, String description, Long authorId) {
        Author author = null;

        if (nonNull(authorId)) {
            author = authorService.getAuthorById(Long.valueOf(authorId));
        }

        Book book = Book.builder().title(title).description(description).author(author).build();

        return repository.findAll(Example.of(book));
    }

    private Book getBook(Long id) {
        Optional<Book> book = repository.findById(id);
        if (book.isEmpty()) {
            String message = String.format("Book with id:%s Not Found", id);
            log.error(message);
            throw new BookNotFoundException(message);
        }
        return book.get();
    }
}
