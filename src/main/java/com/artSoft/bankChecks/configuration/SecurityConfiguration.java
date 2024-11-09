package com.artSoft.bankChecks.configuration;

import com.artSoft.bankChecks.handelException.exception.CustomAuthenticationException;
import com.artSoft.bankChecks.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.PrintWriter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfiguration implements WebMvcConfigurer {

   @Autowired
   private JwtAuthenticationFilter jwtAuthFilter;
   @Autowired
   private AuthenticationProvider authenticationProvider;
   @Autowired
   private LogoutHandler logoutHandler;
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(WHITE_LIST_URL).permitAll()

                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler())
                                .authenticationEntryPoint(errorAuthenticationEntryPoint())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(this.logoutHandler)
                                .logoutSuccessHandler(logoutSuccessHandler())
                );

        return http.build();
    }

   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration corsConfiguration = new CorsConfiguration();
      corsConfiguration.addAllowedOriginPattern("*");
//      corsConfiguration.addAllowedOrigin("http://50.62.182.187:80");
//      corsConfiguration.addAllowedOrigin("http://50.62.182.187:443");
//      corsConfiguration.addAllowedOrigin("http://localhost:4200");
      corsConfiguration.addAllowedMethod("*");
      corsConfiguration.addAllowedHeader("*");
      corsConfiguration.setAllowCredentials(true);
      source.registerCorsConfiguration("/**", corsConfiguration);
      return new CorsFilter(source);
   }

   private void writeJsonResponse(HttpServletResponse response, int status, String message) {
      response.setStatus(status);
      response.setContentType("application/json");
      try (PrintWriter writer = response.getWriter()) {
         writer.write("{\"message\": \"" + message + "\"}");
      }catch (Exception e){
         throw new CustomAuthenticationException("Failed to write JSON response, msg: " + e.getMessage());
      }
   }

   private AccessDeniedHandler accessDeniedHandler() {
      return (request, response, accessDeniedException) ->
              writeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN,
                      String.format("Access Denied: [%s] You do not have permission to access this resource.",request.getUserPrincipal().getName()));
   }

   private LogoutSuccessHandler logoutSuccessHandler() {
      return (request, response, authentication) -> {
         SecurityContextHolder.clearContext();
         writeJsonResponse(response, HttpServletResponse.SC_OK, "Logout successful");
      };
   }

   public AuthenticationEntryPoint errorAuthenticationEntryPoint() {
      return (request, response, authException) ->
              writeJsonResponse(response, HttpServletResponse.SC_BAD_REQUEST, authException.getMessage());
   }

}
