package com.polimi.carzone.dto.response;

import com.polimi.carzone.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UtenteManagerResponseDTO {
    private long id;
    private String email;
    private String nome;
    private String cognome;
    private String username;
    private LocalDate dataNascita;
    private Ruolo ruolo;
}
