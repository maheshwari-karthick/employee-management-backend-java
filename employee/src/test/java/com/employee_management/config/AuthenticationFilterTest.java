package com.employee_management.config;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationFilterTest {
    @Autowired
    private AuthenticationFilter authenticationFilter;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void shouldDoFilterInternal() throws ServletException, IOException {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader("username")).thenReturn("ADMIN");
        when(request.getHeader("password")).thenReturn("ADMIN");
        when(request.getServletPath()).thenReturn("/authenticate");
        when(request.getMethod()).thenReturn("POST");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //when
        authenticationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(response).setStatus(HttpStatus.OK.value());

    }

    @Test
    public void shouldThrowUnauthorizedWhenDoFilterInternal() throws ServletException, IOException {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader("username")).thenReturn("ADMIN");
        when(request.getHeader("password")).thenReturn("ADMIN");
        when(request.getServletPath()).thenReturn("/authenticate");
        when(request.getMethod()).thenReturn("POST");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenThrow(new NullPointerException(""));

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //when
        authenticationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

    @Test
    public void shouldNotAuthenticateWhenWrongURLPassed() throws ServletException, IOException {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        when(request.getHeader("username")).thenReturn("ADMIN");
        when(request.getHeader("password")).thenReturn("ADMIN");
        when(request.getServletPath()).thenReturn("/user");
        when(request.getMethod()).thenReturn("POST");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(isA(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(Mockito.<ServletRequest>any(), Mockito.<ServletResponse>any());

        //when
        authenticationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain).doFilter(isA(ServletRequest.class), isA(ServletResponse.class));

    }


}
