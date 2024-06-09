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

//annotazione per definire una classe di configurazione
@Configuration
//annotazione per abilitare la sicurezza web in Spring
@EnableWebSecurity
public class GestoreFilterChain {
    //la classe si occupa di mappare gli URL degli endpoint con i relativi permessi di accesso

    //dichiarazione delle dipendenze necessarie
    private final FilterJwt filterJwt;
    private final AuthenticationProvider authenticationProvider;

    //costruttore della classe
    public GestoreFilterChain(FilterJwt filterJwt, AuthenticationProvider authenticationProvider) {
        this.filterJwt = filterJwt;
        this.authenticationProvider = authenticationProvider;
    }

    //metodo per creare un bean di tipo SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //il metodo disabilita il CSRF
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                //configura la sicurezza delle richieste HTTP
                .authorizeHttpRequests(auth->auth
                        //mappatura dei singoli URL per garantire che siano rispettati i relativi permessi di accesso
                        //le distinzioni di ruoli vengono fatte tramite le enum Ruolo
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
                        .requestMatchers("api/v1/veicolo/veicoliDisponibiliESelezionato/**").hasRole(Ruolo.MANAGER.toString())
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
                        .requestMatchers("/api/v1/appuntamento/recensioniCliente/**").hasRole(Ruolo.CLIENTE.toString())
                        .requestMatchers("/api/v1/appuntamento/trovaPerModifica/**").hasRole(Ruolo.MANAGER.toString())
                        //tutte le altre richieste sono permesse a tutti gli utenti in quanto è  obiettivo dell'applicazione essere chiara con l'utente
                        .anyRequest().permitAll()
                        //configurazione della gestione delle sessioni come stateless
                ).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //disabilitazione del CORS
                .cors(AbstractHttpConfigurer::disable)
                //aggiunta dell'AuthenticationProvider
                .authenticationProvider(authenticationProvider)
                //aggiunta del filtro FilterJwt
                .addFilterBefore(filterJwt, UsernamePasswordAuthenticationFilter.class);

        //ritorno dell'oggetto SecurityFilterChain
        return httpSecurity.build();
    }

}


