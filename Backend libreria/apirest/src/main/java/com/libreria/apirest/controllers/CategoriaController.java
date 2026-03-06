package com.libreria.apirest.controllers;

import java.io.IOException;
import java.util.Locale.Category;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libreria.apirest.DTO.categoria.CategoriaResponse;
import com.libreria.apirest.DTO.categoria.CreateCategoriaRequest;
import com.libreria.apirest.DTO.user.LoginResponse;
import com.libreria.apirest.models.Categoria;
import com.libreria.apirest.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    //!Model Atribute para trabajar con archivos
    public ResponseEntity<?> create(@ModelAttribute CreateCategoriaRequest request){
        try{
                CategoriaResponse categoria = categoriaService.create(request); 
                return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
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



    @GetMapping
    public ResponseEntity<?> findAll(){
        try{
                List<CategoriaResponse> categorias = categoriaService.findAll();
                return ResponseEntity.ok(categorias);
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
                categoriaService.delete(id);;
                return ResponseEntity.ok(true);
            }catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", e.getMessage(),
                        "statusCode", HttpStatus.BAD_REQUEST.value()
                ));
            }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @ModelAttribute CreateCategoriaRequest request){
        try{
                CategoriaResponse response = categoriaService.update(id,request);;
                return ResponseEntity.ok(response);
            }catch (RuntimeException e){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
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
}
