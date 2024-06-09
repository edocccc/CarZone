package com.polimi.carzone.security;

import com.polimi.carzone.persistence.repository.UtenteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

//annotazione per definire una classe di configurazione
@Configuration
public class BeanConfigJwt {
    //la classe si occupa di creare i bean necessari per la gestione dell'autenticazione tramite JWT

    //dichiarazione della repository degli utenti
    private final UtenteRepository utenteRepo;

    //costruttore della classe
    public BeanConfigJwt(UtenteRepository utenteRepo) {
        this.utenteRepo = utenteRepo;
    }

    //metodo per creare un bean di tipo UserDetailsService
    @Bean
    public UserDetailsService userDetailsService(){
        //ritorna una lambda che prende in input uno username e restituisce l'utente corrispondente se esiste, altrimenti lancia un'eccezione
        return username -> utenteRepo.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utente non trovato"));
    }

    //metodo per creare un bean di tipo PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        //ritorna un oggetto di tipo BCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    //metodo per creare un bean di tipo AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        //creazione di un oggetto di tipo DaoAuthenticationProvider
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        //settaggio del password encoder
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        //settaggio del user details service
        authenticationProvider.setUserDetailsService(userDetailsService());
        //ritorno dell'oggetto
        return authenticationProvider;
    }

    //metodo per creare un bean di tipo AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        //ritorno dell'oggetto AuthenticationManager
        return config.getAuthenticationManager();
    }


}
