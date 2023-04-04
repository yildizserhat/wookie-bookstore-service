package com.yildiz.serhat.wookiebookstoreservice.repository;

import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Author;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
