package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VeicoloResponseDTO {
    private String marca;
    private String modello;
    private double prezzo;
}
