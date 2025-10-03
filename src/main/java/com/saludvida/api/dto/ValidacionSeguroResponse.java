package com.saludvida.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidacionSeguroResponse {
    private String estado;
    private String mensaje;
    private Object datosSeguro;
}
