package com.libreria.apirest.services;

import com.libreria.apirest.DTO.user.CreateUserRequest;
import com.libreria.apirest.models.Role;
import com.libreria.apirest.models.User;
import com.libreria.apirest.models.UserRol;
import com.libreria.apirest.repositories.RoleRepository;
import com.libreria.apirest.repositories.UserRepository;
import com.libreria.apirest.repositories.UserRolRepository;
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

    // Injeccion a RoleRepository
    @Autowired
    private RoleRepository roleRepository;

    // Injeccion a UserRolRepository
    @Autowired
    private UserRolRepository userRolRepository;


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



        // Guardar el usuario guardado en una variable
        User savedUser = userRepository.save(user);

        // Buscar el rol cliente en la tabla de rol
        Role clienteRole = roleRepository.findById("CLIENT").orElseThrow(
                () -> new RuntimeException("El Rol de clienten o existe")
        );

        // Guardar el userrol (el usuario guardado y el rol de cliente)
        UserRol userRol = new UserRol(savedUser, clienteRole);

        // Guardar el usuario
        userRolRepository.save(userRol);


        return  savedUser;
    }
}
