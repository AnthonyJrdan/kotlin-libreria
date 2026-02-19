package com.libreria.apirest.models;

import com.libreria.apirest.models.id.UserRolId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 255)
    private String apellido;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String telefono;

    @Column(length = 255, nullable = true)
    private String imagen;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(name = "notification_token", length = 255, nullable = true)
    private String notificationToken;


    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    // Anotacion uno a muchos
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRol> userRoles = new HashSet<>();

    public User () {}

    // Funcion para actualizar los registros
    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

}
