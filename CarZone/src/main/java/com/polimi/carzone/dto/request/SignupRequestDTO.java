package com.polimi.carzone.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequestDTO {
    private String email;
    private LocalDate dataNascita;
    private String username;
    private String password;
    private String passwordRipetuta;

}
