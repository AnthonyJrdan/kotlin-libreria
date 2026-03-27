package com.libreria.apirest.models.id;



import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CarritoLibroId implements Serializable {
    
    @Column(name = "id_libro")
    private Long idLibro;

    @Column(name = "id_carrito")
    private Long idCarrito;

    public CarritoLibroId() {}

    public CarritoLibroId(Long idLibro, Long idCarrito){
        this.idLibro = idLibro;
        this.idCarrito = idCarrito;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof CarritoLibroId)) return false;
        CarritoLibroId carritoLibroId = (CarritoLibroId) obj;
        return Objects.equals(idCarrito, carritoLibroId.idCarrito) && Objects.equals(idLibro, carritoLibroId.idLibro);

    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarrito, idLibro);
    }
}
