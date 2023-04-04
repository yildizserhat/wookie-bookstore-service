package com.yildiz.serhat.wookiebookstoreservice.repository;

import com.yildiz.serhat.wookiebookstoreservice.domain.entity.User;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Hidden
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
