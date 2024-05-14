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
                        .requestMatchers("/api/v1/utente/login").permitAll()
                        .requestMatchers("/api/v1/utente/signup").permitAll()
                        .requestMatchers("/api/v1/utente/registraDipendente").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/utentiManager").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/trova/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/modifica/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/elimina/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/trovaClienti").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/trovaDipendenti").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/utente/manager/**").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/veicolo/veicoli").permitAll()
                        .requestMatchers("/api/v1/veicolo/dettagli/").permitAll()
                        .requestMatchers("/api/v1/veicolo/cerca").authenticated()
                        .requestMatchers("/api/v1/veicolo/registraVendita").hasAnyRole(Ruolo.MANAGER.toString(), Ruolo.DIPENDENTE.toString())
                        .requestMatchers("/api/v1/veicolo/veicoliConDettagli").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/veicolo/elimina/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/veicolo/modifica/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("api/v1/veicolo/veicoliDisponibili").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/veicolo/aggiungiVeicolo").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/appuntamento/prenota").hasRole(Ruolo.CLIENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/dipendente/**").hasRole(Ruolo.DIPENDENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/appuntamentiLiberi").hasRole(Ruolo.DIPENDENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/prendiInCarico ").hasRole(Ruolo.DIPENDENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/recensioni/").hasRole(Ruolo.DIPENDENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/dipendentiConRecensioni").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/appuntamento/trovaPerManager").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/appuntamento/prenotaPerManager").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/appuntamento/elimina/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/appuntamento/modifica/").hasRole(Ruolo.MANAGER.toString())
                        .requestMatchers("/api/v1/appuntamento/appuntamentiCliente/").hasRole(Ruolo.CLIENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/lasciaRecensione").hasRole(Ruolo.CLIENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/recensioniCliente/").hasRole(Ruolo.CLIENTE.toString())
                        .anyRequest().permitAll()
                ).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filterJwt, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}


