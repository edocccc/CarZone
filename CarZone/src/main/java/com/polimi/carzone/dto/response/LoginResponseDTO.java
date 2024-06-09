package com.polimi.carzone.dto.response;

import com.polimi.carzone.model.Ruolo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// annotazione lombok per generare automaticamente i metodi getter, setter
@Data
// annotazione lombok per generare automaticamente il costruttore con tutti i parametri
@AllArgsConstructor
// annotazione lombok per generare automaticamente il costruttore vuoto
@NoArgsConstructor
public class LoginResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private long id;
    private String email;
    private String nome;
    private String cognome;
    private String username;
    private LocalDate dataNascita;
    private Ruolo ruolo;
    private String token;
}