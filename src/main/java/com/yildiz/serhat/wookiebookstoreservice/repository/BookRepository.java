package com.yildiz.serhat.wookiebookstoreservice.repository;

import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Book;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(Author author);
}
