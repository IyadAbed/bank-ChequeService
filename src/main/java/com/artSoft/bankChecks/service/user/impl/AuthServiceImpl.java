package com.artSoft.bankChecks.service.user.impl;

import com.artSoft.bankChecks.handelException.exception.CustomAuthenticationException;
import com.artSoft.bankChecks.handelException.exception.NotFoundException;
import com.artSoft.bankChecks.handelException.exception.UnauthorizedException;
import com.artSoft.bankChecks.mapper.UserMapper;
import com.artSoft.bankChecks.model.user.documents.User;
import com.artSoft.bankChecks.model.user.dto.request.AuthRequest;
import com.artSoft.bankChecks.model.user.dto.request.UserRequest;
import com.artSoft.bankChecks.model.user.dto.response.AuthResponse;
import com.artSoft.bankChecks.model.user.dto.response.MessageResponse;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import com.artSoft.bankChecks.repository.user.UserRepo;
import com.artSoft.bankChecks.security.JwtAuthenticationFilter;
import com.artSoft.bankChecks.service.user.AuthService;
import com.artSoft.bankChecks.service.user.JwtService;
import com.artSoft.bankChecks.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userServices;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public MessageResponse register(UserRequest request, HttpServletResponse response) {
        User user = userMapper.toEntity(request);
        userServices.validateEmailAndPhoneUniqueness(user.getEmail(), user.getPhone(), null);

        this.userRepo.save(user);
//        Map<String,Object> map = new HashMap<>();
//        map.put("role",user.getRole().name());
//        String jwtToken = this.jwtService.generateToken(map,user);
//        String refreshToken = this.jwtService.generateRefreshToken(map, user);
//        return new AuthResponse(jwtToken, refreshToken);
        return new MessageResponse("User registered successfully");
    }

    @Override
    public AuthResponse login(AuthRequest request, HttpServletResponse response) throws CustomAuthenticationException{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            if (userRepo.findByEmailAndStatus(request.getEmail(), UserStatus.ACTIVE).isEmpty()) {
                throw new CustomAuthenticationException("Invalid email address");
            } else {
                throw new CustomAuthenticationException("Incorrect password");
            }
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new CustomAuthenticationException("Authentication failed: " + e.getMessage());
        }

        User user = userRepo.findByEmailAndStatus(request.getEmail(),UserStatus.ACTIVE).orElseThrow(() -> {
            log.error("User not found for Email: {}", request.getEmail());
            return new NotFoundException("User not found, may be because is deleted or blocked");
        });

        Map<String,Object> map = new HashMap<>();
        map.put("role",user.getRole().name());
        map.put("username",user.getEmail());
        String jwtToken = jwtService.generateToken(map, user);
        String refreshToken = jwtService.generateRefreshToken(map, user);

        log.info("User authenticated successfully: {}", user.getUsername());
        return new AuthResponse(jwtToken, refreshToken);
    }

    @Override
    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String token = jwtAuthenticationFilter.tokenExtractor(request);

        if (token == null) {
            log.warn("Refresh token is null. Unable to refresh access token.");
            throw new UnauthorizedException("the user not authorize. Refresh token is null");
        }

        try {
            final String email = this.jwtService.extractUsername(token);
            if (email != null) {
                User user = userRepo.findByEmailAndStatus(email,UserStatus.ACTIVE).orElseThrow(() -> new NotFoundException("User not found, may be because is deleted or blocked"));
                if (jwtService.isTokenValid(token, user)) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("role",user.getRole().name());
                    map.put("username",user.getEmail());
                    String accessToken = jwtService.generateToken(map,user);
                    String refreshToken = jwtService.generateRefreshToken(map,user);

                    log.info("Access token refreshed successfully for user: {}", email);
                    return new AuthResponse(accessToken, refreshToken);
                } else {
                    log.warn("Invalid refresh token provided for user: {}", email);
                    throw new UnauthorizedException("The refresh token is invalid");
                }
            }
        }catch (Exception e){
            this.jwtAuthenticationFilter.handleUnauthorizedResponse(response, e.getMessage());
        }
        log.warn("The phone in the refresh token is null");
        throw new UnauthorizedException("The phone in the refresh token is null");
    }

}
