package com.libreria.apirest.DTO.user;

import lombok.Data;

@Data
public class LoginResponse {
    
    private String token;
    private UserResponse user;
}
