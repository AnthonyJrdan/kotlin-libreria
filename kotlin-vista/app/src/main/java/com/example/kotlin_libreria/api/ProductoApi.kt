package com.example.kotlin_libreria.api

import com.example.kotlin_libreria.model.Producto
import retrofit2.Response
import retrofit2.http.GET

interface ProductoApi {
    @GET("libros")
    suspend fun getLibros(): Response<List<Producto>>
}
