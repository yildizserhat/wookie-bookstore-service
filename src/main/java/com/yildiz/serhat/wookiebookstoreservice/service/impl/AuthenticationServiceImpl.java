package com.yildiz.serhat.wookiebookstoreservice.service.impl;



import com.yildiz.serhat.wookiebookstoreservice.configuration.JwtService;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.AuthenticationRequest;
import com.yildiz.serhat.wookiebookstoreservice.controller.request.RegisterRequest;
import com.yildiz.serhat.wookiebookstoreservice.controller.response.AuthenticationResponse;
import com.yildiz.serhat.wookiebookstoreservice.domain.Role;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.Token;
import com.yildiz.serhat.wookiebookstoreservice.domain.TokenType;
import com.yildiz.serhat.wookiebookstoreservice.domain.entity.User;
import com.yildiz.serhat.wookiebookstoreservice.repository.TokenRepository;
import com.yildiz.serhat.wookiebookstoreservice.repository.UserRepository;
import com.yildiz.serhat.wookiebookstoreservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.firstName())
                .lastname(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()));
        var user = repository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
