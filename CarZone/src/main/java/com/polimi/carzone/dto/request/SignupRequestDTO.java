package com.polimi.carzone.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequestDTO {
    @NotBlank(message = "Inserisci una email")
    @Email(message = "Inserisci un'email valida")
    private String email;
    @NotNull(message = "Inserisci la data di nascita")
    @Past(message = "La data di nascita deve essere passata")
    private LocalDate dataNascita;
    @NotBlank(message = "Inserisci un username")
    private String username;
    @NotBlank(message = "Inserisci una password")
    private String password;
    @NotBlank(message = "Ripeti la password")
    private String passwordRipetuta;

}
