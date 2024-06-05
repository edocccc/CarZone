package com.polimi.carzone.security;

import com.polimi.carzone.model.Ruolo;
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

@Service
public class TokenUtil {

    private final UtenteService utenteService;

    @Value("${codice.jwt.key}")
    private String key;

    public TokenUtil(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    private SecretKey generaChiave() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generaToken(Utente utente){
        String ruolo = utente.getRuolo().toString();
        String username = utente.getUsername();
        String email = utente.getEmail();

        long durata = 1000L*60*60*24;

        return Jwts.builder().claims()
                .add("ruolo", ruolo)
                .add("email", email)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + durata))
                .and()
                .signWith(generaChiave())
                .compact();
    }

    public Claims estraiClaims(String token){
        JwtParser parser = Jwts.parser()
                .verifyWith(generaChiave())
                .build();
        return parser.parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(String token){
        return estraiClaims(token).getSubject();
    }

    public Utente getUtenteFromToken(String token){
        String username = getSubject(token);
        return utenteService.findByUsername(username);
    }

}
