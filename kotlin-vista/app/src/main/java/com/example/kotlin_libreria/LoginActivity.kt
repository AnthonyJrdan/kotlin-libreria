package com.example.kotlin_libreria

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.kotlin_libreria.api.RetrofitClient
import com.example.kotlin_libreria.databinding.ActivityLoginBinding
import com.example.kotlin_libreria.model.LoginRequest
import com.example.kotlin_libreria.model.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupListeners()

        intent.getStringExtra("email")?.let { email ->
            binding.editEmail.setText(email)
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.editEmail.error = "Ingrese su correo"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.editPassword.error = "Ingrese su contraseña"
                return@setOnClickListener
            }

            login(email, password)
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun login(email: String, password: String) {
        binding.btnLogin.isEnabled = false
        binding.btnLogin.text = "Cargando..."

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        SessionManager.saveUserSession(email, authResponse.user.nombre, authResponse.token)
                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido ${authResponse.user.nombre}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Credenciales incorrectas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = getString(R.string.txtentrarbiblioteca)
            }
        }
    }
}
