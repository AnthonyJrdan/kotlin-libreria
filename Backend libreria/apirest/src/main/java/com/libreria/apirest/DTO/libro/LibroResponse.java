package com.libreria.apirest.DTO.libro;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LibroResponse {

    private Long id;
    @JsonProperty("id_categoria")
    private Long idCategoria;
    private String nombre;
    private String autor;
    private String descripcion;
    private Double precio;
    private String imagen1;
    private String imagen2;
    @JsonProperty("fecha_creacion")
    private LocalDateTime createdAt;

    @JsonProperty("fecha_actualizacion")
    private LocalDateTime updateAt;
}
