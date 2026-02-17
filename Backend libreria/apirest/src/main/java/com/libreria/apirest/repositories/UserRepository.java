package com.libreria.apirest.repositories;

import com.libreria.apirest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Validar que el email exista o no en la bd
    //El nombre debe ser exactamente para que el jpa lo reconosca
    boolean existsByEmail(String email);

}
