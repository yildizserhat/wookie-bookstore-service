package com.yildiz.serhat.wookiebookstoreservice.service;

import com.yildiz.serhat.wookiebookstoreservice.controller.request.BookCreateRequestDTO;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Book;

import java.util.List;

public interface BookService {

    void createBook(BookCreateRequestDTO request);

    List<Book> getAllBooksByAuthorId(Long id);

    Book getBookById(Long id);

    Book unpublishBook(Long id);

    void deleteBookById(Long id);

    Book updateBookById(Long id, BookCreateRequestDTO request);

    List<Book> getBooksWithCriteria(String title, String description, Long author);
}
