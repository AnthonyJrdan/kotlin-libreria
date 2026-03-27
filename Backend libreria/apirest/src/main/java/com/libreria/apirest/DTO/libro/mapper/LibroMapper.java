package com.libreria.apirest.DTO.libro.mapper;

import org.springframework.stereotype.Component;

import com.libreria.apirest.DTO.libro.LibroResponse;
import com.libreria.apirest.config.ApiConfig;
import com.libreria.apirest.models.Libro;

@Component
public class LibroMapper {

    public LibroResponse tLibroResponse(Libro libro) {
        LibroResponse response = new LibroResponse();
        response.setId(libro.getId());
        response.setNombre(libro.getNombre());
        response.setAutor(libro.getAutor());
        response.setDescripcion(libro.getDescipcion());
        response.setPrecio(libro.getPrecio());
        response.setCreatedAt(libro.getCreateAt());
        response.setUpdateAt(libro.getUpdateAt());
        response.setIdCategoria(libro.getCategoria().getId());

        if(libro.getImagen1() != null)
        {
            response.setImagen1(ApiConfig.BASE_URL + libro.getImagen1());
        }

        if(libro.getImagen2() != null)
        {
            response.setImagen2(ApiConfig.BASE_URL + libro.getImagen2());
        }
        return response;
    }
    
}
