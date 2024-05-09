package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ModificaUtenteRequestDTO {
    private String email;
    private String nome;
    private String cognome;
    private String username;
    private LocalDate dataNascita;
    private String ruolo;
}
