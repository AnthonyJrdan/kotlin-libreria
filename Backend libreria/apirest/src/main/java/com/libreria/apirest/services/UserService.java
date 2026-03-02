package com.libreria.apirest.services;

import com.libreria.apirest.DTO.rol.RoleDTO;
import com.libreria.apirest.DTO.user.CreateUserRequest;
import com.libreria.apirest.DTO.user.CreateUserResponse;
import com.libreria.apirest.DTO.user.LoginRequest;
import com.libreria.apirest.DTO.user.LoginResponse;
import com.libreria.apirest.models.Role;
import com.libreria.apirest.models.User;
import com.libreria.apirest.models.UserRol;
import com.libreria.apirest.repositories.RoleRepository;
import com.libreria.apirest.repositories.UserRepository;
import com.libreria.apirest.repositories.UserRolRepository;
import com.libreria.apirest.utils.JwtUtil;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


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

    // Injeccion JwT
    @Autowired
    private JwtUtil jwtUtil; 

    // Guardar informacion a la bd
    @Transactional
    public CreateUserResponse create(CreateUserRequest request)
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


        CreateUserResponse response = new CreateUserResponse();
        response.setId(savedUser.getId());
        response.setNombre(savedUser.getNombre());
        response.setApellido(savedUser.getApellido());
        response.setImagen(savedUser.getImagen());
        response.setTelefono(savedUser.getTelefono());
        response.setEmail(savedUser.getEmail());

        //
        List<Role> roles = roleRepository.findAllByUserRoles_User_Id(savedUser.getId());
        List<RoleDTO> roleDTOS = roles.stream()
                        .map(role -> new RoleDTO(role.getId(), role.getNombre(), role.getImagen(), role.getRuta()))
                                .toList();


        response.setRoles(roleDTOS);



        return  response;
    }


    // Devolver usuario con token
    @Transactional
    public LoginResponse login(LoginRequest request)
    {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow
        (() -> new RuntimeException("El Email o password no son validos"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("El Email o password no son validos");
        }

        String token = jwtUtil.generateToken(user);
        List<Role> roles = roleRepository.findAllByUserRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS = roles.stream()
                        .map(role -> new RoleDTO(role.getId(), role.getNombre(), role.getImagen(), role.getRuta()))
                                .toList();

        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setNombre(user.getNombre());
        createUserResponse.setApellido(user.getApellido());
        createUserResponse.setImagen(user.getImagen());
        createUserResponse.setTelefono(user.getTelefono());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setRoles(roleDTOS);

        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(createUserResponse);

        return response;
    }


    // Devolver el usuario por id
    @Transactional
    public CreateUserResponse findById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow
        (() -> new RuntimeException("El Email o password no son validos"));
    List<Role> roles = roleRepository.findAllByUserRoles_User_Id(user.getId());
        List<RoleDTO> roleDTOS = roles.stream()
                        .map(role -> new RoleDTO(role.getId(), role.getNombre(), role.getImagen(), role.getRuta()))
                                .toList();

        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setId(user.getId());
        createUserResponse.setNombre(user.getNombre());
        createUserResponse.setApellido(user.getApellido());
        createUserResponse.setImagen(user.getImagen());
        createUserResponse.setTelefono(user.getTelefono());
        createUserResponse.setEmail(user.getEmail());
        createUserResponse.setRoles(roleDTOS);

        return createUserResponse;
    }
}
