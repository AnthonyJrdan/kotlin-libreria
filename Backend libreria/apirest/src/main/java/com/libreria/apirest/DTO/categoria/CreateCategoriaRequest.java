package com.libreria.apirest.DTO.categoria;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class CreateCategoriaRequest {
    
    private String nombre;
    private String descripcion;
    private MultipartFile file;
}
