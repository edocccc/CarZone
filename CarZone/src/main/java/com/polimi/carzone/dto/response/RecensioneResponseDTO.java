package com.polimi.carzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecensioneResponseDTO {
    private String nomeCliente;
    private String cognomeCliente;
    private Integer voto;
    private String testo;
}
