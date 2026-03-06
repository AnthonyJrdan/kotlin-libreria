package com.libreria.apirest.DTO.categoria.mapper;

import org.springframework.stereotype.Component;

import com.libreria.apirest.DTO.categoria.CategoriaResponse;
import com.libreria.apirest.config.ApiConfig;
import com.libreria.apirest.models.Categoria;

@Component
public class CategoriaMapper {

    public CategoriaResponse toCategoriaResponse(Categoria categoria){
        CategoriaResponse response = new CategoriaResponse();
        response.setId(categoria.getId());
        response.setNombre(categoria.getNombre());
        response.setDescripcion(categoria.getDescripcion());
        response.setCreatedAt(categoria.getCreateAt());
        response.setUpdateAt(categoria.getUpdateAt());


        if(categoria.getImagen() != null){
            String imageUrl = ApiConfig.BASE_URL + categoria.getImagen();
            response.setImagen(imageUrl);
        }

        return response;
    }
    
}
