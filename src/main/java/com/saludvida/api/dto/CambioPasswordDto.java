package com.saludvida.api.dto;

import lombok.Data;

@Data
public class CambioPasswordDto {
    private String token; 
    private String newPassword;
}