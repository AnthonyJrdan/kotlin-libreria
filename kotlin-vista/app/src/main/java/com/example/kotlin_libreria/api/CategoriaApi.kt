package com.example.kotlin_libreria.api

import com.example.kotlin_libreria.model.Categoria
import com.example.kotlin_libreria.model.LibroApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoriaApi {
    @GET("categorias")
    suspend fun getCategorias(): Response<List<Categoria>>

    @GET("libros/categorias/{id}")
    suspend fun getLibrosPorCategoria(@Path("id") categoriaId: Int): Response<List<LibroApi>>
}
