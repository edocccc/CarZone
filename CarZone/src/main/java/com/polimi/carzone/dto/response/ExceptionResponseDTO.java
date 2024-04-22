package com.polimi.carzone.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ExceptionResponseDTO {

    private LocalDateTime timestamp;

    private Map<String,String> errori;
}
