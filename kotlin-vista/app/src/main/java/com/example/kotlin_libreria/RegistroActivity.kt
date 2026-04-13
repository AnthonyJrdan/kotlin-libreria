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
import com.example.kotlin_libreria.databinding.ActivityRegistroBinding
import com.example.kotlin_libreria.model.RegistroRequest
import com.example.kotlin_libreria.model.SessionManager
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val telefono = binding.etTelefono.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!validarCampos(nombre, apellido, email, telefono, password)) return@setOnClickListener

            registrar(nombre, apellido, email, telefono, password)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun validarCampos(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        password: String
    ): Boolean {
        var esValido = true

        if (nombre.isEmpty()) {
            binding.etNombre.error = "Ingrese su nombre"
            esValido = false
        }
        if (apellido.isEmpty()) {
            binding.etApellido.error = "Ingrese su apellido"
            esValido = false
        }
        if (email.isEmpty()) {
            binding.etEmail.error = "Ingrese su correo"
            esValido = false
        }
        if (telefono.isEmpty()) {
            binding.etTelefono.error = "Ingrese su teléfono"
            esValido = false
        }
        if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese su contraseña"
            esValido = false
        }

        return esValido
    }

    private fun registrar(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        password: String
    ) {
        binding.btnRegistrar.isEnabled = false
        binding.btnRegistrar.text = "Registrando..."

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.authApi.register(
                    RegistroRequest(email, password, nombre, apellido, telefono)
                )
                if (response.isSuccessful) {
                    val nombreCompleto = "$nombre $apellido"
                    val authResponse = response.body()
                    if (authResponse != null) {
                        SessionManager.saveUserSession(email, nombreCompleto, authResponse.token)
                    }
                    Toast.makeText(
                        this@RegistroActivity,
                        "¡Registro exitoso! Bienvenido $nombreCompleto",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RegistroActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@RegistroActivity,
                        "Error al registrar. Intente de nuevo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@RegistroActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.btnRegistrar.isEnabled = true
                binding.btnRegistrar.text = getString(R.string.textregistrarr)
            }
        }
    }
}
