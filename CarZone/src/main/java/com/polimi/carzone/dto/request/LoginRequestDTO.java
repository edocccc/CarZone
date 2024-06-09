package com.polimi.carzone.dto.request;

import lombok.Data;

//annotazione di Lombok per generare i metodi getter e setter
@Data
public class LoginRequestDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private String username;
    private String password;
}
