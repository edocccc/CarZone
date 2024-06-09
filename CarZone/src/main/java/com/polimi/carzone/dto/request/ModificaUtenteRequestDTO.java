package com.polimi.carzone.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// annotazione di lombok per generare automaticamente i metodi getter
@Getter
// annotazione di lombok per generare automaticamente il costruttore con tutti i parametri
@AllArgsConstructor
// annotazione di lombok per generare automaticamente il costruttore senza parametri
@NoArgsConstructor
public class ModificaUtenteRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String email;
    private String nome;
    private String cognome;
    private String username;
    private LocalDate dataNascita;
    private String ruolo;
}
