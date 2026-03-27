package com.libreria.apirest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libreria.apirest.models.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long>{
    
    List<Libro> findByCategoriaId(Long idCategoria);
}
