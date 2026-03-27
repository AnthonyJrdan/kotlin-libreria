package com.libreria.apirest.DTO.libro;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateLibroRequest{
    
    private String nombre;
    private String autor;
    private String descripcion;
    private Double precio;
    //! El nombre de la variable debe llamarse file o files
    private MultipartFile[] files; 
}
