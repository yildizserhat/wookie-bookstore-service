package com.yildiz.serhat.wookiebookstoreservice.service.impl;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthorCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.exception.AuthorNotFoundException;
import com.yildiz.serhat.wookiebookstoreservice.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @InjectMocks
    private AuthorServiceImpl authorService;
    @Mock
    private AuthorRepository repository;


    @Test
    public void shouldCreateCar() {
        AuthorCreateRequestDTO createRequestDTO =
                new AuthorCreateRequestDTO("Stephen", "King", "Richard Bachman");

        when(repository.save(any())).thenReturn(Author.builder().build());
        authorService.createAuthor(createRequestDTO);

        verify(repository, atLeastOnce()).save(any());
    }

    @Test
    public void shouldGetAuthorById() {
        when(repository.findById(1L)).thenReturn(Optional.of(Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build()));

        Author authorById = authorService.getAuthorById(1L);

        assertEquals(authorById.getFirstName(), "Stephen");
        assertEquals(authorById.getLastName(), "King");
        assertEquals(authorById.getPseudonym(), "Richard Bachman");
    }

    @Test
    public void shouldThrowExceptionIfAuthorNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class,
                () -> authorService.getAuthorById(1L));
    }

    @Test
    public void shouldGetAllAuthors() {
        when(repository.findAll()).thenReturn(List.of(Author.builder()
                .id(1L)
                .firstName("Stephen")
                .lastName("King")
                .pseudonym("Richard Bachman")
                .build()));

        List<Author> allAuthors = authorService.getAllAuthors();

        assertEquals(allAuthors.size(), 1);
        assertEquals(allAuthors.get(0).getFirstName(), "Stephen");
        assertEquals(allAuthors.get(0).getLastName(), "King");
        assertEquals(allAuthors.get(0).getPseudonym(), "Richard Bachman");
    }

}