package com.artSoft.bankChecks.service.user;

import com.artSoft.bankChecks.handelException.exception.CustomAuthenticationException;
import com.artSoft.bankChecks.model.user.dto.request.AuthRequest;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.AuthResponse;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    MessageResponse register(UserRequest request, HttpServletResponse response);

    AuthResponse login(AuthRequest request, HttpServletResponse response) throws CustomAuthenticationException;

    AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
}
