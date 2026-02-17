package com.libreria.apirest.repositories;

import com.libreria.apirest.models.UserRol;
import com.libreria.apirest.models.id.UserRolId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolRepository extends JpaRepository<UserRol, UserRolId> {
}
