package com.libreria.apirest.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(length = 36)
    private String id = UUID.randomUUID().toString();

    @Column(length = 36, unique = true, nullable = false)
    private String nombre;

    @Column(length = 255, nullable = false)
    private String imagen;

    @Column(length = 255, nullable = false)
    private String ruta;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    // Anotacion uno a muchos
    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRol> userRoles = new HashSet<>();

    // Constructor
    public Role() {}
    // Funcion para actualizar los registros
    @PreUpdate
    public void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
