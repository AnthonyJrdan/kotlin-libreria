package com.example.kotlin_libreria.api

import com.example.kotlin_libreria.model.AuthResponse
import com.example.kotlin_libreria.model.LoginRequest
import com.example.kotlin_libreria.model.RegistroRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegistroRequest): Response<AuthResponse>
}
