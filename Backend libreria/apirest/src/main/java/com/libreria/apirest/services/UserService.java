package com.libreria.apirest.services;

import com.libreria.apirest.DTO.rol.RoleDTO;
import com.libreria.apirest.DTO.user.CreateUserRequest;
import com.libreria.apirest.DTO.user.UserResponse;
import com.libreria.apirest.DTO.user.mapper.UserMapper;
import com.libreria.apirest.DTO.user.LoginRequest;
import com.libreria.apirest.DTO.user.LoginResponse;
import com.libreria.apirest.DTO.user.UpdateUserRequest;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    private UserMapper userMapper;

    // Guardar informacion a la bd
    @Transactional
    public LoginResponse create(CreateUserRequest request)
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


        // ! TOKEN DE SESION
        String token = jwtUtil.generateToken(user);
        // Obtenemos el rol por el id del usuario
        List<Role> roles = roleRepository.findAllByUserRoles_User_Id(savedUser.getId());
        // Instanciamos el DTO del login response
        LoginResponse response = new LoginResponse();
        // Enviamos el token
        response.setToken("Bearer " + token);
        // guardamos la informacion 
        response.setUser(userMapper.toUserResponse(user, roles));




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
        LoginResponse response = new LoginResponse();
        response.setToken("Bearer " + token);
        response.setUser(userMapper.toUserResponse(user, roles));

        return response;
    }


    // Devolver el usuario por id
    @Transactional
    public UserResponse findById(Long id)
    {
        User user = userRepository.findById(id).orElseThrow
        (() -> new RuntimeException("El Email o password no son validos"));
    List<Role> roles = roleRepository.findAllByUserRoles_User_Id(user.getId());


        return userMapper.toUserResponse(user, roles);
    }


    // Actualizar usuario con imagen


    @Transactional
    public UserResponse updateUserWhitImage(Long id, UpdateUserRequest request) throws IOException
    {
        User user = userRepository.findById(id).orElseThrow
        (() -> new RuntimeException("El Email o password no son validos"));


        // ! Validaciones
        if(request.getNombre() != null){
            user.setNombre(request.getNombre());
        } 

        if(request.getApellido() != null){
            user.setApellido(request.getApellido());
        }

        if(request.getTelefono() != null){
            user.setTelefono(request.getTelefono());
        }

        // * Agregamos ruta y actualizamos imagen
        if(request.getArchivo() != null && !request.getArchivo().isEmpty()) {
            String uploadDir = "uploads/usuarios/" + user.getId();
            String nombreArchivo= request.getArchivo().getOriginalFilename();
            String rutaImagen = Paths.get(uploadDir, nombreArchivo).toString();

            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getArchivo().getInputStream(), Paths.get(rutaImagen), StandardCopyOption.REPLACE_EXISTING);
            user.setImagen("/" + rutaImagen.replace("\\", "/"));
        }


        userRepository.save(user);




    List<Role> roles = roleRepository.findAllByUserRoles_User_Id(user.getId());


        return userMapper.toUserResponse(user, roles);
    }
}
