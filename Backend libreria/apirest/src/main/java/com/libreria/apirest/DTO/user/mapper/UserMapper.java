package com.libreria.apirest.DTO.user.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.libreria.apirest.DTO.rol.RoleDTO;
import com.libreria.apirest.DTO.user.UserResponse;
import com.libreria.apirest.config.ApiConfig;
import com.libreria.apirest.models.Role;
import com.libreria.apirest.models.User;

@Component
public class UserMapper {
    
    public UserResponse toUserResponse(User user, List<Role> roles) {
        List<RoleDTO> roleDTOS = roles.stream()
                        .map(role -> new RoleDTO(role.getId(), role.getNombre(), role.getImagen(), role.getRuta()))
                                .toList();

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setApellido(user.getApellido());
        response.setTelefono(user.getTelefono());
        response.setEmail(user.getEmail());
        response.setRoles(roleDTOS);
        
        if(user.getImagen() != null){
            String imageUrl = ApiConfig.BASE_URL + user.getImagen();
            response.setImagen(imageUrl);
            
        }

        return response;
    };
} 
