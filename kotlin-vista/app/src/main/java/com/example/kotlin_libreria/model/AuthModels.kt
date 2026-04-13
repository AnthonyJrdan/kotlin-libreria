package com.example.kotlin_libreria.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegistroRequest(
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String,
    val telefono: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val email: String,
    val nombre: String,
    val apellido: String,
    val telefono: String?,
    val imagen: String?,
    val notification_token: String?,
    val roles: List<Rol>
)

data class Rol(
    val id: String,
    val nombre: String,
    val imagen: String,
    val ruta: String
)
