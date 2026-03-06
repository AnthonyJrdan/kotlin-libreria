package com.libreria.apirest.DTO.categoria;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CategoriaResponse {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;

    @JsonProperty("fecha_creacion")
    private LocalDateTime createdAt;

    @JsonProperty("fecha_actualizacion")
    private LocalDateTime updateAt;
}
