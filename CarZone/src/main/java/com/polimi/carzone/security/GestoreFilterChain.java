package com.polimi.carzone.security;

import com.polimi.carzone.model.Ruolo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class GestoreFilterChain {

    private final FilterJwt filterJwt;
    private final AuthenticationProvider authenticationProvider;

    public GestoreFilterChain(FilterJwt filterJwt, AuthenticationProvider authenticationProvider) {
        this.filterJwt = filterJwt;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/signup").permitAll()
                        //TODO permettere le cose che non richiedono autenticazione
                        .requestMatchers("/manager/**").hasRole(Ruolo.MANAGER.toString())
                        .anyRequest().permitAll()
                ).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filterJwt, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}


