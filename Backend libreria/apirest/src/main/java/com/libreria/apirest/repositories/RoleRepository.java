package com.libreria.apirest.repositories;

import com.libreria.apirest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByNombre(String nombre);
}
