package com.yildiz.serhat.wookiebookstoreservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthorCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.repository.AuthorRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository repository;

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void shouldCreateNewAuthor() {
        AuthorCreateRequestDTO createRequestDTO =
                new AuthorCreateRequestDTO("Stephen", "King", "Richard Bachman");


        mockMvc.perform(post("/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isCreated());

        List<Author> all = repository.findAll();

        assertEquals(1, all.size());
        assertEquals("Stephen", all.get(0).getFirstName());
        assertEquals("King", all.get(0).getLastName());
        assertEquals("Richard Bachman", all.get(0).getPseudonym());
    }

    @Test
    @WithMockUser
    @SneakyThrows
    void shouldGetAllAuthors() {
        AuthorCreateRequestDTO createRequestDTO =
                new AuthorCreateRequestDTO("Stephen", "King", "Richard Bachman");

        mockMvc.perform(get("/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDTO)))
                .andExpect(status().isOk());
    }

}