package com.libreria.apirest.repositories;
import com.libreria.apirest.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    
    
}
