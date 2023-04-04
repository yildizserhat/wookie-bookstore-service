package com.yildiz.serhat.wookiebookstoreservice.service.impl;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthorCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.exception.AuthorNotFoundException;
import com.yildiz.serhat.wookiebookstoreservice.repository.AuthorRepository;
import com.yildiz.serhat.wookiebookstoreservice.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Override
    public void createAuthor(AuthorCreateRequestDTO request) {
        Author author = Author.buildAuthorFromRequest(request);
        Author savedAuthor = repository.save(author);
        log.info("Author saved with id:{}", savedAuthor.getId());
    }

    @Override
    public Author getAuthorById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(String.format("User not found with Id :%s", id)));
    }

    @Override
    public List<Author> getAllAuthors() {
        return repository.findAll();
    }
}
