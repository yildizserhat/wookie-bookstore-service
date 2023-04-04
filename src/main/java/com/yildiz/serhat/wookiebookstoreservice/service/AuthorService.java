package com.yildiz.serhat.wookiebookstoreservice.service;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthorCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;

import java.util.List;

public interface AuthorService {

    void createAuthor(AuthorCreateRequestDTO request);

    Author getAuthorById(Long id);

    List<Author> getAllAuthors();
}
