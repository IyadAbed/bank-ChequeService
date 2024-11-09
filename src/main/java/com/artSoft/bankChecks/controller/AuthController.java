package com.artSoft.bankChecks.controller;

import com.artSoft.bankChecks.model.user.dto.request.AuthRequest;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.AuthResponse;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.service.user.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.login(request, response));

    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody UserRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.register(request, response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.refreshToken(request, response));
    }
}
