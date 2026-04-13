package com.example.kotlin_libreria.api

import com.example.kotlin_libreria.model.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.33:3000/"

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        SessionManager.authToken?.let { token ->
            val authValue = if (token.startsWith("Bearer ")) token else "Bearer $token"
            request.addHeader("Authorization", authValue)
        }
        chain.proceed(request.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val productoApi: ProductoApi = retrofit.create(ProductoApi::class.java)
    val categoriaApi: CategoriaApi = retrofit.create(CategoriaApi::class.java)
}
