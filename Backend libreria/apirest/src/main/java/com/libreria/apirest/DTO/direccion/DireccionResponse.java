package com.libreria.apirest.DTO.direccion;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DireccionResponse {
    
    private Long id;
    @JsonProperty("id_user")
    private Long idUser;
    private String direccion;
    private String localidad;


    @JsonProperty("fecha_creacion")
    private LocalDateTime createdAt;

    @JsonProperty("fecha_actualizacion")
    private LocalDateTime updateAt;
}
