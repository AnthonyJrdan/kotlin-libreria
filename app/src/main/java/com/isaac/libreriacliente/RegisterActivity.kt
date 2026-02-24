package com.isaac.libreriacliente

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnCreateAccount.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (name.isEmpty()) {
                etName.error = "Ingresa tu nombre"
                etName.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                etEmail.error = "Ingresa tu correo"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (pass.length < 6) {
                etPassword.error = "Mínimo 6 caracteres"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    Toast.makeText(this, "Cuenta creada ✅", Toast.LENGTH_SHORT).show()

                    // ✅ Ir a Home y limpiar historial
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    val msg = when (e) {
                        is FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
                        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil (mínimo 6 caracteres)."
                        is FirebaseAuthInvalidCredentialsException -> "El correo no es válido."
                        else -> "Error al registrar: ${e.localizedMessage}"
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
