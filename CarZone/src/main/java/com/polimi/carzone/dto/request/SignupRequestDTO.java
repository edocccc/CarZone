package com.polimi.carzone.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequestDTO {
    private String email;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String username;
    private String password;
    private String passwordRipetuta;

}
