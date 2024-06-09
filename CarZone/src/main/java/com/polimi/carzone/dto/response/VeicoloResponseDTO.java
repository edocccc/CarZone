package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

//annotazione lombok per generare i metodi getter e setter
@Data
//annotazione lombok per generare un costruttore con tutti i campi della classe
@AllArgsConstructor
public class VeicoloResponseDTO {
    // definizione dello standard dato dal dto in base agli attributi
    private long id;
    private String marca;
    private String modello;
    private double prezzo;
    private String stato;
    private byte[] immagine;
}
