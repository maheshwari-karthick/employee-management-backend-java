package com.employee_management.config;

import com.employee_management.filter.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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

    private final JWTFilter jwtFilter;
    private final ApplicationConfiguration applicationConfiguration;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> {
                            authHttp.requestMatchers("/user").permitAll();
                            authHttp.requestMatchers("/user/all").permitAll();
                            authHttp.requestMatchers("/employee").permitAll();
                            authHttp.requestMatchers("/employee/**").permitAll();
                            authHttp.requestMatchers("/department").permitAll();
                            authHttp.requestMatchers("/department/**").permitAll();
                            authHttp.requestMatchers("/swagger-ui/**").permitAll();
                            authHttp.requestMatchers("/v3/api-docs/**").permitAll();
                            authHttp.requestMatchers("/swagger-resources/**").permitAll();
                            authHttp.requestMatchers("/webjars/**").permitAll();
                            authHttp.requestMatchers("/h2-console/**").permitAll();
                            authHttp.anyRequest().authenticated();
                        }
                )
               // .formLogin(l -> l.defaultSuccessUrl("/employee"))
              //  .logout(l -> l.logoutSuccessUrl("/user"))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(applicationConfiguration.authenticationProvider())
                .build();
    }

}
