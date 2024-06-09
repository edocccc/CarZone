package com.polimi.carzone.security;

import com.polimi.carzone.model.Utente;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//annotazione che definisce la classe come componente
@Component
public class FilterJwt extends OncePerRequestFilter {
    //classe che estende OncePerRequestFilter e si occupa di filtrare le richieste per controllare la presenza di un token JWT

    //dichiarazione del bean TokenUtil
    @Autowired
    private TokenUtil tokenUtil;

    //metodo che si occupa di filtrare le richieste
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //estrazione del token JWT dall'header della richiesta
        String authCode = request.getHeader("Authorization");
        //controllo se il token è presente e se inizia con "Bearer"
        if(authCode != null && authCode.startsWith("Bearer")){
            //se presente, estrazione del token vero e proprio
            String token = authCode.substring(7);
            //estrazione dell'utente dal token
            Utente utente = tokenUtil.getUtenteFromToken(token);
            //creazione di un oggetto di tipo UsernamePasswordAuthenticationToken con l'utente estratto
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(utente, null, utente.getAuthorities());
            //settaggio dei dettagli dell'autenticazione
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //settaggio dell'oggetto di autenticazione nel contesto di sicurezza
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //passaggio della richiesta al prossimo filtro
        filterChain.doFilter(request, response);
    }
}
