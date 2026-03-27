package com.libreria.apirest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libreria.apirest.DTO.direccion.CreateDireccionRequest;
import com.libreria.apirest.DTO.direccion.DireccionResponse;
import com.libreria.apirest.models.Direccion;
import com.libreria.apirest.models.User;
import com.libreria.apirest.repositories.DireccionRepository;
import com.libreria.apirest.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;
     

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DireccionResponse create(CreateDireccionRequest request){
        User user= userRepository.findById(request.getIdUser()).orElseThrow(
            () -> new RuntimeException("La direccion no se encuentra")
        );

        Direccion direccion = new Direccion();
        direccion.setUser(user);
        

    }
}
