package com.libreria.apirest.DTO.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.libreria.apirest.DTO.rol.RoleDTO;
import lombok.Data;

import java.util.List;

@Data
public class CreateUserResponse {

    public Long id;
    public String nombre;
    public String apellido;
    public String email;
    public String telefono;
    public String imagen;

    // Devuelve el campo con snake case
    @JsonProperty("notification_token")
    public String notificationToken;

    List<RoleDTO> roles;


}
