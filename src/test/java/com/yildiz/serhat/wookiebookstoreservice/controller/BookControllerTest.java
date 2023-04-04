package com.yildiz.serhat.wookiebookstoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthorCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.BookCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Book;
import com.yildiz.serhat.wookiebookstoreservice.repository.AuthorRepository;
import com.yildiz.serhat.wookiebookstoreservice.repository.BookRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        AuthorCreateRequestDTO createRequestDTO =
                new AuthorCreateRequestDTO("Stephen", "King", "Richard Bachman");

        authorRepository.save(Author.buildAuthorFromRequest(createRequestDTO));
    }

    @AfterEach
    public void tearDown() {
        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldCreateNewBook() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");


        mockMvc.perform(post("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isCreated());

        List<Book> all = bookRepository.findAll();

        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getAuthor().getFirstName(), "Stephen");
        assertEquals(all.get(0).getAuthor().getLastName(), "King");
        assertEquals(all.get(0).getTitle(), title);
        assertEquals(all.get(0).getDescription(), description);
        assertEquals(all.get(0).getPrice().setScale(2), price.setScale(2));
        assertEquals(all.get(0).getImageUrl(), url);
        assertTrue(all.get(0).getActive());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldNotCreateNewBookWithoutAuthor() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        authorRepository.deleteAll();


        mockMvc.perform(post("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldGetValidationErrorForPrice() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(-19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        mockMvc.perform(post("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldGetBookById() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        bookRepository.save(Book.buildBookFromRequest(bookCreateRequestDTO));

        mockMvc.perform(get("/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description))
                .andExpect(jsonPath("$.imageUrl").value(url))
                .andExpect(jsonPath("$.price").value(price));
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldDeleteBookById() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        bookRepository.save(Book.buildBookFromRequest(bookCreateRequestDTO));

        mockMvc.perform(delete("/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isOk());

        List<Book> all = bookRepository.findAll();

        assertEquals(all.size(), 0);
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldUnpublishBook() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        bookRepository.save(Book.buildBookFromRequest(bookCreateRequestDTO));

        mockMvc.perform(put("/v1/books/unpublish/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isOk());

        List<Book> all = bookRepository.findAll();

        assertEquals(all.size(), 1);
        assertFalse(all.get(0).getActive());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldUpdateBook() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        bookRepository.save(Book.buildBookFromRequest(bookCreateRequestDTO));

        BookCreateRequestDTO bookCreateRequestDTO2 = new BookCreateRequestDTO(title,
                description,
                "url",
                price, "1");

        mockMvc.perform(put("/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO2)))
                .andExpect(status().isOk());

        List<Book> all = bookRepository.findAll();

        assertEquals(all.size(), 1);
        assertEquals(all.get(0).getImageUrl(), "url");
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldGetAllBooks() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        mockMvc.perform(post("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isCreated());

        BookCreateRequestDTO bookCreateRequestDTO2 = new BookCreateRequestDTO(title,
                description,
                "url",
                price, "1");

        mockMvc.perform(post("/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/books/author/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    public void shouldGetBookWithCriteria() {
        String description = "In the summer of 1989, a group of bullied kids band together to destroy a shape-shifting monster, which disguises itself as a clown and preys on the children of Derry, their small Maine town.";
        String url = "https://m.media-amazon.com/images/M/MV5BYjg1YTRkNzQtODgyYi00MTQ5LThiMDYtNDJjMWRjNTdjZDZlXkEyXkFqcGdeQXVyNTAyODkwOQ@@._V1_.jpg";
        BigDecimal price = BigDecimal.valueOf(19.90);

        String title = "IT";
        BookCreateRequestDTO bookCreateRequestDTO = new BookCreateRequestDTO(title,
                description,
                url,
                price, "1");

        bookRepository.save(Book.buildBookFromRequest(bookCreateRequestDTO));

        mockMvc.perform(get("/v1/books?title=IT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateRequestDTO)))
                .andExpect(status().isOk());
    }

}