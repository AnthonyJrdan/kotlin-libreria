package com.libreria.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libreria.apirest.models.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long>{
    
}
