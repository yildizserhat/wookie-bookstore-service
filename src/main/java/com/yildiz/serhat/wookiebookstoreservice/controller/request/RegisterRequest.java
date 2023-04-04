package com.yildiz.serhat.wookiebookstoreservice.controller.request;

import jakarta.validation.constraints.NotNull;


public record RegisterRequest(
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String email,
        @NotNull String password
) {
}
