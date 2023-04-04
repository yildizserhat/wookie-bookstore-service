package com.yildiz.serhat.wookiebookstoreservice.controller.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookCreateRequestDTO(
        @NotNull String title,
        @NotNull String description,
        @NotNull String imageUrl,
        @Positive
        @Digits(integer = 3, fraction = 2) BigDecimal price,
        @NotNull
        String authorId) {
}
