package com.yildiz.serhat.wookiebookstoreservice.controller.request;

import jakarta.validation.constraints.NotNull;


public record AuthenticationRequest(
        @NotNull String email,
        @NotNull String password) {
}
