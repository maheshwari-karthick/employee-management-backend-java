package com.employee_management.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    private AuthenticationManager authenticationManager;

    private final ApplicationConfiguration applicationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> {
                            authHttp.requestMatchers("/swagger-ui/**").permitAll();
                            authHttp.requestMatchers("/v3/api-docs/**").permitAll();
                            authHttp.requestMatchers("/swagger-resources/**").permitAll();
                            authHttp.requestMatchers("/webjars/**").permitAll();
                            authHttp.requestMatchers("/h2-console/**").permitAll();
                            authHttp.anyRequest().authenticated();
                        }
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(applicationConfiguration.authenticationProvider())
                .build();
    }

    @Bean
    public AuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new AuthenticationFilter("/authenticate", secretKey);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        // Provide your JwtAuthorizationFilter bean implementation here
        return new JwtAuthorizationFilter("/**", secretKey);
    }

}


