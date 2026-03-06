package com.libreria.apirest.services;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.libreria.apirest.DTO.categoria.CategoriaResponse;
import com.libreria.apirest.DTO.categoria.CreateCategoriaRequest;
import com.libreria.apirest.DTO.categoria.mapper.CategoriaMapper;
import com.libreria.apirest.models.Categoria;
import com.libreria.apirest.repositories.CategoriaRepository;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper categoriaMapper;

    // * Crear Categoria
    @Transactional
    public CategoriaResponse create(CreateCategoriaRequest request) throws IOException {


        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        // almacenar nombre y categoria
        Categoria categoriaSaved = categoriaRepository.save(categoria);
        
        // Guardar imagen
        if(request.getFile() != null && !request.getFile().isEmpty()){
            String uploadDir = "uploads/categorias/" + categoriaSaved.getId();
            String fileName = request.getFile().getOriginalFilename();
            String filePath = Paths.get(uploadDir, fileName).toString();

            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getFile().getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            categoriaSaved.setImagen("/" + filePath.replace("\\", "/"));
            categoriaRepository.save(categoriaSaved);
        }

        return categoriaMapper.toCategoriaResponse(categoriaSaved);
        
    }

    // * Actualizar Categoria
    @Transactional
    public CategoriaResponse update(Long id, CreateCategoriaRequest request) throws IOException {

        Categoria categoria = categoriaRepository.findById(id).orElseThrow(
            () -> new RuntimeException("La Categoria no existe") 
        );


        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        // Guardar imagen
        if(request.getFile() != null && !request.getFile().isEmpty()){

            if(categoria.getImagen() != null) {
                Path previusImagePath = Paths.get("." + categoria.getImagen());
                if(Files.exists(previusImagePath)){
                    Files.delete(previusImagePath);
                }
            }


            String uploadDir = "uploads/categorias/" + categoria.getId();
            String fileName = request.getFile().getOriginalFilename();
            String filePath = Paths.get(uploadDir, fileName).toString();

            Files.createDirectories(Paths.get(uploadDir));
            Files.copy(request.getFile().getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            categoria.setImagen("/" + filePath.replace("\\", "/"));
            
        }

        Categoria updateCategoria = categoriaRepository.save(categoria);
        return categoriaMapper.toCategoriaResponse(updateCategoria);
        
    }

    // * Devolver las categorias
    @Transactional
    public List<CategoriaResponse> findAll() {
        List<Categoria> categorias = categoriaRepository.findAll();
        // Mapea categorias a categoria response
        return categorias.stream().map(categoria ->  {
            return categoriaMapper.toCategoriaResponse(categoria);
        }).toList();
    }

    // ! eliminar categoria
    @Transactional
    public void delete(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow(
            () -> new RuntimeException("La Categoria no existe") 
        );
        categoriaRepository.delete(categoria);
    }
}
