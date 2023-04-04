package com.yildiz.serhat.wookiebookstoreservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.BookCreateRequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private Author author;

    @Column(name = "active")
    private Boolean active;

    public static Book buildBookFromRequest(BookCreateRequestDTO requestDTO) {
        return Book.builder()
                .title(requestDTO.title())
                .description(requestDTO.description())
                .imageUrl(requestDTO.imageUrl())
                .price(requestDTO.price())
                .active(true)
                .build();
    }

    public static Book updateBook(Book book, BookCreateRequestDTO requestDTO) {
        book.setDescription(requestDTO.description());
        book.setPrice(requestDTO.price());
        book.setTitle(requestDTO.title());
        book.setImageUrl(requestDTO.imageUrl());
        return book;
    }
}
