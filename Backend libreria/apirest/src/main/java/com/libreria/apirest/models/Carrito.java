package com.libreria.apirest.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.Collate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrito")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;


    @Column(length = 255)
    private String status = "PAGADO";

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now();


    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

}
