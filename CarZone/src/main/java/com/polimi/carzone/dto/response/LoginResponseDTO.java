package com.polimi.carzone.dto.response;

import com.polimi.carzone.model.Ruolo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoginResponseDTO {
    private long id;
    private String email;
    private String nome;
    private String cognome;
    private String username;
    private LocalDate dataNascita;
    private Ruolo ruolo;
    private String token;
}