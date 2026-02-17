package com.libreria.apirest.services;

import com.libreria.apirest.DTO.user.CreateUserRequest;
import com.libreria.apirest.models.User;
import com.libreria.apirest.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // Injectar el repository , esto nos sirve para no crear la instancia
    @Autowired
    private UserRepository userRepository;
    // Injeccion de la clase
    @Autowired
    private PasswordEncoder passwordEncoder;


    // Guardar informacion a la bd
    @Transactional
    public User create(CreateUserRequest request)
    {
        if (userRepository.existsByEmail(request.email)) {
            throw new RuntimeException("El Correo ya esta registrado");
        }
        User user = new User();
        user.setNombre(request.nombre);
        user.setApellido(request.apellido);
        user.setTelefono(request.telefono);
        user.setEmail(request.email);

        // Encriptar la contraseña
        String encryptedPassword = passwordEncoder.encode(request.password);
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }
}
