package com.artSoft.bankChecks.security;

import com.artSoft.bankChecks.handelException.exception.CustomAuthenticationException;

import com.artSoft.bankChecks.service.user.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    private static final String BEARER = "Bearer ";
    private static final String UNAUTHORIZED_ERROR_MESSAGE = "{\"error\": \"the user is not authorized\"}";

    public String tokenExtractor(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "access_token");
        if (cookie != null) {
            return cookie.getValue();
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            return header.replace(BEARER, "");
        }
        return null;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Skip filter for specific paths
        if (request.getServletPath().contains("/api/v1/auth") || request.getServletPath().contains("/api/v1/users/factory-warehouse-items")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorization = tokenExtractor(request);
        if (authorization == null) {
            filterChain.doFilter(request, response);
            log.warn("User is not authorized");
            return;
        }

        try {
            final String userPhone = this.jwtService.extractUsername(authorization);
            if (userPhone != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                handleTokenValidation(request, authorization, userPhone);
            } else {
                handleUnauthorizedResponse(response, "You do not have permission to access this resource. " +
                        "Invalid condition: userEmail is null or authentication object is already present");
                return;
            }
        } catch (Exception e) {
            handleUnauthorizedResponse(response, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void handleUnauthorizedResponse(HttpServletResponse response, String error) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(UNAUTHORIZED_ERROR_MESSAGE + ",\n\t\"msg\": \"" + error + "\"}");
        } catch (Exception e) {
            throw new CustomAuthenticationException("Failed to write JSON response, msg: " + e.getMessage());
        }
    }

    private void handleTokenValidation(HttpServletRequest request, String authorization, String userPhone) throws IOException {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userPhone);

        // Validate token using the JWT service, no DB check for token state
        if (this.jwtService.isTokenValid(authorization, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            handleUnauthorizedResponse(null, "The Token is not valid");
        }
    }

}
