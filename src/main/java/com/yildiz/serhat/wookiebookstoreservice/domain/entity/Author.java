package com.yildiz.serhat.wookiebookstoreservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthorCreateRequestDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "author")
public class Author extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "pseudonym")
    private String pseudonym;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    public static Author buildAuthorFromRequest(AuthorCreateRequestDTO requestDTO) {
        return Author.builder()
                .firstName(requestDTO.firstName())
                .lastName(requestDTO.lastName())
                .pseudonym(requestDTO.pseudonym())
                .build();
    }
}
