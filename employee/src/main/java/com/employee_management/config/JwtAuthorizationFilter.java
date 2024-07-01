package com.employee_management.config;

import com.employee_management.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String pathPattern;
    private final String secretKey;
    private final RequestMatcher excludedPaths = new OrRequestMatcher(
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/webjars/****"),
            new AntPathRequestMatcher("/v3/api-docs/**")
    );

    @Autowired
    UserService userService;


    public JwtAuthorizationFilter(String pathPattern, String secretKey) {
        this.pathPattern = pathPattern;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws IOException, ServletException {

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String username = null;
        String jwt = null;

        if (excludedPaths.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.isBlank(authorizationHeader)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized");
            return;
        }

        if (authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            Jws<Claims> claimsJws;
            try {
                claimsJws = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwt);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT Token.");
                return;
            }
            Claims body = claimsJws.getBody();
            username = body.getSubject();
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;
            try {
                userDetails = userService.loadUserByUsername(username);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Invalid JWT Token.");
                return;
            }

            if (userDetails != null && validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("JWT Token Expired");
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    private boolean validateToken(String token, @NonNull UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }

}
