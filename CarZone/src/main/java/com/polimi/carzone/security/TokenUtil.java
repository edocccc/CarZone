package com.polimi.carzone.security;

import com.polimi.carzone.model.Utente;
import com.polimi.carzone.persistence.service.UtenteService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
//annotazione per indicare a Spring che la classe è un service
@Service
public class TokenUtil {
    //classe che si occupa della generazione e validazione dei token JWT

    //dichiarazione del service dell'utente
    private final UtenteService utenteService;

    //iniezione della variabile key dal file application.properties
    @Value("${codice.jwt.key}")
    private String key;

    //costruttore della classe
    public TokenUtil(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    //metodo per generare la chiave segreta
    private SecretKey generaChiave() {
        //ritorno della chiave segreta generata a partire dalla stringa key tramite l'algoritmo HMAC SHA
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    //metodo per generare un token JWT a partire da un utente
    public String generaToken(Utente utente){
        //estrazione delle informazioni dell'utente
        String ruolo = utente.getRuolo().toString();
        String username = utente.getUsername();
        String email = utente.getEmail();

        //definizione della durata del token
        long durata = 1000L*60*60*24;

        //creazione e ritorno del token JWT
        return Jwts.builder().claims()
                //aggiunta delle informazioni dell'utente al token
                .add("ruolo", ruolo)
                .add("email", email)
                //definizione del subject del token come lo username dell'utente
                .subject(username)
                //definizione della data di creazione del token
                .issuedAt(new Date(System.currentTimeMillis()))
                //definizione della data di scadenza del token
                .expiration(new Date(System.currentTimeMillis() + durata))
                .and()
                //firma del token con la chiave segreta
                .signWith(generaChiave())
                .compact();
    }

    //metodo per estrarre le informazioni dal token
    public Claims estraiClaims(String token){
        //creazione di un oggetto JwtParser per verificare il token con la chiave segreta
        JwtParser parser = Jwts.parser()
                //verifica del token con la chiave segreta
                .verifyWith(generaChiave())
                //costruzione del parser
                .build();
        //ritorno delle informazioni estratte dal token
        return parser.parseSignedClaims(token)
                //estrazione dei claims
                .getPayload();
    }

    //metodo per estrarre il subject dal token
    public String getSubject(String token){
        //ritorno del subject estratto dal token con il metodo estraiClaims
        return estraiClaims(token).getSubject();
    }

    //metodo per estrarre l'utente dal token
    public Utente getUtenteFromToken(String token){
        //estrazione dello username dal token
        String username = getSubject(token);
        //cerco l'utente con lo username estratto dal token e lo ritorno
        return utenteService.findByUsername(username);
    }

}
