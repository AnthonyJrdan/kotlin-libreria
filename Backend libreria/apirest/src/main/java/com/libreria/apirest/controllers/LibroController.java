package com.libreria.apirest.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libreria.apirest.DTO.categoria.CategoriaResponse;
import com.libreria.apirest.DTO.categoria.CreateCategoriaRequest;
import com.libreria.apirest.DTO.libro.CreateLibroRequest;
import com.libreria.apirest.DTO.libro.LibroResponse;
import com.libreria.apirest.DTO.libro.UpdateLibroRequest;
import com.libreria.apirest.services.LibroService;

@RestController
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;


    @PostMapping(consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    //!Model Atribute para trabajar con archivos
    public ResponseEntity<?> create(@ModelAttribute CreateLibroRequest request){
        try{
                LibroResponse response = libroService.create(request); 
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.BAD_REQUEST.value()
                ));
            }catch(IOException e)
            {
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", e.getMessage(),
                "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }
    }

    @PutMapping("/{id}")
    //!Model Atribute para trabajar con archivos
    public ResponseEntity<?> update(
        @PathVariable Long id,
        @ModelAttribute UpdateLibroRequest request){
        try{
                LibroResponse response = libroService.update(id, request); 
                return ResponseEntity.ok(response);
            }catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.BAD_REQUEST.value()
                ));
            }catch(IOException e)
            {
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", e.getMessage(),
                "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }
    }


    @GetMapping("/categorias/{idCategoria}")
    //!Model Atribute para trabajar con archivos
    public ResponseEntity<?> findByCategoriaId(@PathVariable Long idCategoria){
        try{
                List<LibroResponse> response = libroService.findByCategoriaId(idCategoria); 
                return ResponseEntity.ok(response);
            }catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.BAD_REQUEST.value()
                ));
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try{
                libroService.delete(id); 
                return ResponseEntity.ok(true);
            }catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.BAD_REQUEST.value()
                ));
            }
    }
}
