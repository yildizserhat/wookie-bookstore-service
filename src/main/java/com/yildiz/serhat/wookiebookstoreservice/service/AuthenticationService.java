package com.yildiz.serhat.wookiebookstoreservice.service;


import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthenticationRequest;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.RegisterRequest;
import com.yildiz.serhat.wookiebookstoreservice.controller.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
