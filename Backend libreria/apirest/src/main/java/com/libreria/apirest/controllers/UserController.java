package com.libreria.apirest.controllers;

import com.libreria.apirest.DTO.user.CreateUserRequest;
import com.libreria.apirest.DTO.user.UserResponse;
import com.libreria.apirest.DTO.user.UpdateUserRequest;
import com.libreria.apirest.services.UserService;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    // @PostMapping
    // public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request)
    // {
    //     UserResponse user = userService.create(request);
    //     return ResponseEntity.ok(user);
    // }S


    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id)
    {
        try{
            UserResponse response = userService.findById(id);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.NOT_FOUND.value()
            ));

        }

    }


    @PutMapping(value = "/upload/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @ModelAttribute UpdateUserRequest request)
    {
        try{
            UserResponse response = userService.updateUserWhitImage(id, request);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.NOT_FOUND.value()
            ));

        } catch (IOException e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "message", e.getMessage(),
                    "statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }

    }
}
