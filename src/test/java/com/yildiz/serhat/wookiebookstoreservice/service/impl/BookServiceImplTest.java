package com.yildiz.serhat.wookiebookstoreservice.service.impl;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.BookCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Book;
import com.yildiz.serhat.wookiebookstoreservice.exception.BookNotFoundException;
import com.yildiz.serhat.wookiebookstoreservice.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository repository;
    @Mock
    private AuthorServiceImpl authorService;

    @Test
    public void shouldCreateBook() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build();

        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        when(authorService.getAuthorById(1L)).thenReturn(author);
        Book book = Book.buildBookFromRequest(bookCreateRequestDTO);
        book.setId(1L);
        when(repository.save(any())).thenReturn(book);

        bookService.createBook(bookCreateRequestDTO);

        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    public void shouldGetAllBooksByAuthor() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build();

        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        Book book = Book.buildBookFromRequest(bookCreateRequestDTO);
        book.setId(1L);
        book.setAuthor(author);

        when(authorService.getAuthorById(1L)).thenReturn(author);
        when(repository.findByAuthor(any())).thenReturn(List.of(book));

        List<Book> allBooksByAuthorId = bookService.getAllBooksByAuthorId(1L);

        assertEquals(allBooksByAuthorId.size(), 1);
        assertEquals(allBooksByAuthorId.get(0).getAuthor().getFirstName(), "Stephen");
        assertEquals(allBooksByAuthorId.get(0).getAuthor().getLastName(), "King");
        assertEquals(allBooksByAuthorId.get(0).getImageUrl(), url);
        assertEquals(allBooksByAuthorId.get(0).getDescription(), description);
        assertEquals(allBooksByAuthorId.get(0).getTitle(), title);
        assertEquals(allBooksByAuthorId.get(0).getPrice(), price);
    }

    @Test
    public void shouldGetBookById() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build();

        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        Book book = Book.buildBookFromRequest(bookCreateRequestDTO);
        book.setId(1L);
        book.setAuthor(author);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        Book bookById = bookService.getBookById(1L);

        assertEquals(bookById.getAuthor().getFirstName(), "Stephen");
        assertEquals(bookById.getAuthor().getLastName(), "King");
        assertEquals(bookById.getImageUrl(), url);
        assertEquals(bookById.getDescription(), description);
        assertEquals(bookById.getTitle(), title);
        assertEquals(bookById.getPrice(), price);
    }

    @Test
    public void shouldThrowExceptionWhenBookNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class,
                () -> bookService.getBookById(1L));
    }

    @Test
    public void shouldUnpublishBook() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build();

        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        Book book = Book.buildBookFromRequest(bookCreateRequestDTO);
        book.setId(1L);
        book.setAuthor(author);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        book.setActive(false);
        when(repository.save(any())).thenReturn(book);

        Book bookById = bookService.unpublishBook(1L);

        assertEquals(bookById.getAuthor().getFirstName(), "Stephen");
        assertEquals(bookById.getAuthor().getLastName(), "King");
        assertEquals(bookById.getImageUrl(), url);
        assertEquals(bookById.getDescription(), description);
        assertEquals(bookById.getTitle(), title);
        assertEquals(bookById.getPrice(), price);
        assertFalse(bookById.getActive());
    }

    @Test
    public void shouldDeleteBookById() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build();

        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        Book book = Book.buildBookFromRequest(bookCreateRequestDTO);
        book.setId(1L);
        book.setAuthor(author);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBookById(1L);

        verify(repository, atLeastOnce()).deleteById(1L);
    }

    @Test
    public void shouldUpdateBook() {
        Author author = Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build();

        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        Book book = Book.buildBookFromRequest(bookCreateRequestDTO);
        book.setId(1L);
        book.setAuthor(author);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        book.setDescription("description");
        when(repository.save(any())).thenReturn(book);

        Book bookById = bookService.unpublishBook(1L);

        assertEquals(bookById.getAuthor().getFirstName(), "Stephen");
        assertEquals(bookById.getAuthor().getLastName(), "King");
        assertEquals(bookById.getImageUrl(), url);
        assertEquals(bookById.getTitle(), title);
        assertEquals(bookById.getPrice(), price);
        assertEquals(bookById.getDescription(), "description");
    }


}