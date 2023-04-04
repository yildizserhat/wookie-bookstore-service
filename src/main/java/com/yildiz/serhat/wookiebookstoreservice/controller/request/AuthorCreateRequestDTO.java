package com.yildiz.serhat.wookiebookstoreservice.controller.request;

import jakarta.validation.constraints.NotNull;

public record AuthorCreateRequestDTO(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String pseudonym) {
}
