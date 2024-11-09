package com.artSoft.bankChecks.configuration;

import com.artSoft.bankChecks.handelException.exception.NotFoundException;
import com.artSoft.bankChecks.model.user.enums.UserStatus;
import com.artSoft.bankChecks.repository.user.UserRepo;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Autowired
    private UserRepo repository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            log.debug("Fetching user by phone: {}", username);
            return this.repository.findByEmailAndStatus(username, UserStatus.ACTIVE)
                    .orElseThrow(() -> new NotFoundException("User With This Phone Not Found"));
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        log.debug("Creating authentication provider bean");
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration config) throws Exception {
        log.debug("Creating authentication manager bean");
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating password encoder bean");
        return new BCryptPasswordEncoder();
    }

}
