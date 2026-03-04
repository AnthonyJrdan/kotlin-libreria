package com.libreria.apirest.DTO.user;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String nombre;
    private String apellido;
    private String telefono;
    private MultipartFile archivo;
    
}
