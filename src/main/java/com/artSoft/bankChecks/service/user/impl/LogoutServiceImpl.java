package com.artSoft.bankChecks.service.user.impl;


import com.artSoft.bankChecks.handelException.exception.UnauthorizedException;
import com.artSoft.bankChecks.security.JwtAuthenticationFilter;
import com.artSoft.bankChecks.service.user.JwtService;
import com.artSoft.bankChecks.service.user.LogoutService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

@Slf4j
@Service
public class LogoutServiceImpl implements LogoutService {


    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void logout(
            @NotNull HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        log.info("Logging out user");

        // Extract token
        final String token = this.jwtAuthenticationFilter.tokenExtractor(request);
        if (token == null) {
            log.warn("Token not found for logout");
            throw new UnauthorizedException("The user is not authorized. Token is null");
        }

        // Invalidate the cookie
        Cookie cookie = WebUtils.getCookie(request, "access_token");
        if (cookie != null) {
            cookie.setMaxAge(0);  // This effectively deletes the cookie
            response.addCookie(cookie);
        }

        // Clear the Authorization header
        response.setHeader(HttpHeaders.AUTHORIZATION, "");

        // Clear the security context (log the user out)
        SecurityContextHolder.clearContext();

        log.info("User logged out successfully");
    }
}