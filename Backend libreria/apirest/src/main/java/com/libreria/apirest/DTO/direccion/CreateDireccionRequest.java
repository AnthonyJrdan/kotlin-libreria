package com.libreria.apirest.DTO.direccion;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateDireccionRequest {
    
    @JsonProperty("id_user")
    private Long idUser;

    private String direccion;

    private String localidad;
}
