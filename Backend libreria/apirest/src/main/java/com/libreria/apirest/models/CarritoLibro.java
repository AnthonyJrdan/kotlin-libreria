package com.libreria.apirest.models;

import com.libreria.apirest.models.id.CarritoLibroId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrito_libro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoLibro {
    
    @EmbeddedId
    private CarritoLibroId id;

    @ManyToOne
    @JoinColumn()
    private Libro libro;
}
