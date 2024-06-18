package com.employee_management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final String pathPattern;
    private final String secretKey;
    @Autowired
    AuthenticationManager authenticationManager;

    public AuthenticationFilter(String pathPattern, String secretKey) {
        this.pathPattern = pathPattern;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isAuthenticationAttempt(request)) {
            try {
                // Extract credentials from request (e.g., JSON payload)
                String username = request.getHeader("username");
                String password = request.getHeader("password");

                // Perform custom authentication logic (e.g., authenticate against database)
                Authentication authentication = authenticate(username, password);

                // Set authenticated authentication object to SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate token and add to response
                if (authentication.isAuthenticated()) {
                    String jwtToken = generateToken(username);
                    response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);

                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType("application/json");

                    Map<String, Object> successResponse = new HashMap<>();
                    successResponse.put("status", HttpStatus.OK.value());
                    successResponse.put("access_token", "Bearer " + jwtToken);
                    successResponse.put("message", "Authentication successful");

                    PrintWriter out = response.getWriter();
                    out.print(new ObjectMapper().writeValueAsString(successResponse));
                    out.flush();
                    return;
                }
            } catch (AuthenticationException e) {
                // Handle authentication failure (e.g., return 401 Unauthorized)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                Map<String, Object> failureResponse = new HashMap<>();
                failureResponse.put("status", HttpStatus.UNAUTHORIZED.value());
                failureResponse.put("message", "Authentication failed");
                return;
            }

            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthenticationAttempt(HttpServletRequest request) {
        return this.pathPattern.equals(request.getServletPath()) && request.getMethod().equals("POST");
    }

    private Authentication authenticate(String username, String password) throws AuthenticationException {
        // Example: Using Spring Security's AuthenticationManager to authenticate
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000)) // 10 days validity
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }
}
