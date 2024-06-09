package com.polimi.carzone.dto.response;

import com.polimi.carzone.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

//annotazione di lombok per generare i metodi getter e setter
@Data
//annotazione di lombok per generare un costruttore per tutti i campi
@RequiredArgsConstructor
//annotazione di lombok per generare un costruttore con tutti i campi
@AllArgsConstructor
public class UtenteManagerResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private long id;
    private String email;
    private String nome;
    private String cognome;
    private String username;
    private LocalDate dataNascita;
    private Ruolo ruolo;
}
