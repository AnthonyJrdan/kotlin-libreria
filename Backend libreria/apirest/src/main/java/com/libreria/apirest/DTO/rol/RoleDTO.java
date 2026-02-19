package com.libreria.apirest.DTO.rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private String id;
    private String nombre;
    private String imagen;
    private String ruta;
}
