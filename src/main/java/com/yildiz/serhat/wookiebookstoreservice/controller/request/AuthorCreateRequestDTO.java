package com.yildiz.serhat.wookiebookstoreservice.controller.request;

public record AuthorCreateRequestDTO(String firstName,
                                     String lastName,
                                     String pseudonym) {
}
