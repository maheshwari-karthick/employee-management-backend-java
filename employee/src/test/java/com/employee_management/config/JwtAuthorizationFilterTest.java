package com.employee_management.config;

import com.employee_management.model.Role;
import com.employee_management.model.User;
import com.employee_management.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtAuthorizationFilterTest {
    private static final String secretKey = "DORj3hb0HtpjcjGWsqSn42luTGTiM9CO1lA6qgb3LOf2reIeueaqBX4VsgaaqQpmgjolA6svr1Ug";
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;
    @MockBean
    private UserService userService;

    @Test
    public void shouldDoFilterInternal() throws ServletException, IOException {
        String jwtToken = generateToken("ADMIN");
        User user = new User(1L, "ADMIN", "test", List.of(Role.ADMIN));

        // mock
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwtToken);
        when(request.getPathInfo()).thenReturn("http://localhost:8080/user/all");
        when(request.getServletPath()).thenReturn("http://localhost:8080/user/all");
        when(userService.loadUserByUsername("ADMIN")).thenReturn(user);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //when
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));
        verify(request).getHeader(eq("Authorization"));
        verify(request, atLeast(1)).getPathInfo();
        verify(request, atLeast(1)).getServletPath();
    }

    @Test
    public void shouldThrowUnauthorizedWhenDoFilter() throws ServletException, IOException {

        User user = new User(1L, "ADMIN", "test", List.of(Role.ADMIN));

        //mock
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");
        when(request.getPathInfo()).thenReturn("http://localhost:8080/user/all");
        when(request.getServletPath()).thenReturn("http://localhost:8080/user/all");
        when(userService.loadUserByUsername("ADMIN")).thenReturn(user);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //When
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(writer).write("Unauthorized");
    }

    @Test
    public void shouldThrowUserNotFoundWhenDoFilter() throws ServletException, IOException {
        String jwtToken = generateToken("ADMIN");
        User user = new User(1L, "ADMIN", "test", List.of(Role.ADMIN));

        //mock
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwtToken);
        when(request.getPathInfo()).thenReturn("http://localhost:8080/user/all");
        when(request.getServletPath()).thenReturn("http://localhost:8080/user/all");
        when(userService.loadUserByUsername("ADMIN")).thenThrow(new UsernameNotFoundException("User not found"));

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //When
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(writer).write("Invalid JWT Token.");
    }

    @Test
    public void shouldThrowInvalidExceptionWhenDoFilter() throws ServletException, IOException {
        String jwtToken = generateToken("ADMIN");

        //mock
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + jwtToken);
        when(request.getPathInfo()).thenReturn("http://localhost:8080/user/all");
        when(request.getServletPath()).thenReturn("http://localhost:8080/user/all");
        when(userService.loadUserByUsername("ADMIN")).thenReturn(null);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //When
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(writer).write("Invalid JWT Token.");
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
