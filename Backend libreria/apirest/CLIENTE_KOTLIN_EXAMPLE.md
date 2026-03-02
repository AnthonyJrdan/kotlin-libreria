# Consumir API REST con Retrofit en Kotlin

## 1. Agregar dependencias en `build.gradle.kts` (app)

```gradle
dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    
    // OkHttp (cliente HTTP)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Coroutines (recomendado para llamadas asíncronas)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
```

---

## 2. Modelos/DTOs en Kotlin

### RoleDTO.kt
```kotlin
data class RoleDTO(
    val id: String,
    val nombre: String,
    val imagen: String?,
    val ruta: String
)
```

### CreateUserRequest.kt
```kotlin
data class CreateUserRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val password: String
)
```

### CreateUserResponse.kt
```kotlin
import com.google.gson.annotations.SerializedName

data class CreateUserResponse(
    val id: Long,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val imagen: String?,
    @SerializedName("notification_token")
    val notificationToken: String?,
    val roles: List<RoleDTO>
)
```

### LoginRequest.kt
```kotlin
data class LoginRequest(
    val email: String,
    val password: String
)
```

### LoginResponse.kt
```kotlin
data class LoginResponse(
    val token: String,
    val user: CreateUserResponse
)
```

### ErrorResponse.kt
```kotlin
data class ErrorResponse(
    val message: String,
    val statusCode: Int
)
```

---

## 3. Interfaz de Servicio Retrofit

### ApiService.kt
```kotlin
import retrofit2.http.*

interface ApiService {
    
    // Auth endpoints
    @POST("/auth/register")
    suspend fun register(@Body request: CreateUserRequest): CreateUserResponse
    
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    // User endpoints
    @GET("/usuarios/{id}")
    suspend fun getUserById(@Path("id") id: Long): CreateUserResponse
    
    @POST("/usuarios")
    suspend fun createUser(@Body request: CreateUserRequest): CreateUserResponse
}
```

---

## 4. Interceptor para agregar Token JWT

### TokenInterceptor.kt
```kotlin
import okhttp3.Interceptor
import okhttp3.Response
import android.content.SharedPreferences

class TokenInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        
        // Obtener el token guardado
        val token = sharedPreferences.getString("auth_token", null)
        
        if (!token.isNullOrEmpty()) {
            // El token ya incluye "Bearer", si es necesario agrégalo
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            request.addHeader("Authorization", authToken)
        }
        
        return chain.proceed(request.build())
    }
}
```

---

## 5. Retrofit Client (Singleton)

### RetrofitClient.kt
```kotlin
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object RetrofitClient {
    
    private const val BASE_URL = "http://localhost:8080/"
    private var retrofit: Retrofit? = null
    private lateinit var sharedPreferences: SharedPreferences
    
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    
    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            // Configurar OkHttpClient
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(TokenInterceptor(sharedPreferences)) // Agregar token automáticamente
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY // Ver request/response completo
                })
                .build()
            
            // Configurar Gson
            val gson: Gson = GsonBuilder()
                .setLenient()
                .create()
            
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }
    
    fun getApiService(): ApiService = getRetrofit().create(ApiService::class.java)
}
```

---

## 6. Repository (Patrón Recomendado)

### AuthRepository.kt
```kotlin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.content.SharedPreferences

class AuthRepository(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) {
    
    suspend fun login(email: String, password: String): Result<LoginResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = apiService.login(request)
                
                // Guardar el token
                sharedPreferences.edit().apply {
                    putString("auth_token", response.token)
                    putString("user_id", response.user.id.toString())
                    apply()
                }
                
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    suspend fun register(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        password: String
    ): Result<CreateUserResponse> = withContext(Dispatchers.IO) {
        try {
            val request = CreateUserRequest(
                nombre = nombre,
                apellido = apellido,
                email = email,
                telefono = telefono,
                password = password
            )
            val response = apiService.register(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getUserById(id: Long): Result<CreateUserResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getUserById(id)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    
    fun logout() {
        sharedPreferences.edit().apply {
            remove("auth_token")
            remove("user_id")
            apply()
        }
    }
    
    fun isLoggedIn(): Boolean {
        return !sharedPreferences.getString("auth_token", null).isNullOrEmpty()
    }
}
```

---

## 7. ViewModel (con LiveData)

### AuthViewModel.kt
```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.login(email, password)
            
            result.onSuccess { response ->
                _loginResponse.value = response
                _errorMessage.value = ""
            }
            result.onFailure { error ->
                _errorMessage.value = error.message ?: "Error desconocido"
            }
            
            _isLoading.value = false
        }
    }
    
    fun register(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.register(nombre, apellido, email, telefono, password)
            
            result.onSuccess { response ->
                _errorMessage.value = "Usuario registrado exitosamente"
            }
            result.onFailure { error ->
                _errorMessage.value = error.message ?: "Error en el registro"
            }
            
            _isLoading.value = false
        }
    }
    
    fun logout() {
        authRepository.logout()
    }
}
```

---

## 8. Ejemplo de uso en una Activity/Fragment

### LoginActivity.kt
```kotlin
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

class LoginActivity : AppCompatActivity() {
    
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar RetrofitClient
        RetrofitClient.initialize(this)
        
        // Observar los cambios
        viewModel.loginResponse.observe(this) { response ->
            Toast.makeText(this, "Bienvenido ${response.user.nombre}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        
        viewModel.errorMessage.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            // Mostrar/ocultar ProgressBar
        }
        
        // Hacer login
        // viewModel.login("joaquiny@gmail.com", "password123")
    }
}
```

---

## 9. Configurar en Application

### MyApp.kt
```kotlin
import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(this)
    }
}
```

En `AndroidManifest.xml`:
```xml
<application
    android:name=".MyApp"
    ...>
```

---

## Resumen de flujo

1. Usuario ingresa email y contraseña
2. ViewModel llama al Repository
3. Repository hace la petición con Retrofit
4. TokenInterceptor agrega automáticamente el token a las solicitudes posteriores
5. Respuesta se guarda en SharedPreferences
6. LiveData notifica a la UI con los cambios

¡Listo! Ahora puedes consumir tu API desde Kotlin.
