package com.example.kotlin_libreria.model

data class LibroApi(
    val id: Int,
    val nombre: String,
    val autor: String,
    val descripcion: String,
    val precio: Double,
    val id_categoria: Int,
    val imagen1: String,
    val imagen2: String,
    val fecha_creacion: String,
    val fecha_actualizacion: String
)
