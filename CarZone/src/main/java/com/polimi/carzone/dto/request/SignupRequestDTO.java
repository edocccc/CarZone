package com.polimi.carzone.dto.request;

import lombok.Data;

import java.time.LocalDate;

//annotazione di Lombok per generare i metodi getter e setter
@Data
public class SignupRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String email;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String username;
    private String password;
    private String passwordRipetuta;

}
