package com.libreria.apirest.repositories;

import com.libreria.apirest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
