package com.isaac.libreriacliente

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val rvLibros = findViewById<RecyclerView>(R.id.rvLibros)


        val btnCarrito = findViewById<Button>(R.id.btnCarrito)

        btnCarrito.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }



        val email = auth.currentUser?.email ?: "Usuario"
        tvWelcome.text = "Bienvenido, $email"

        // ✅ Datos locales (por ahora)
        val libros = listOf(
            Libro("Cien Años de Soledad", "Gabriel García Márquez", 35.00, R.drawable.cien_anos),
            Libro("El Principito", "Antoine de Saint-Exupéry", 25.50, R.drawable.principito),
            Libro("Don Quijote de la Mancha", "Miguel de Cervantes", 40.00, R.drawable.don_quijote),
            Libro("Harry Potter y la Piedra Filosofal", "J.K. Rowling", 45.90, R.drawable.harry_potter),
            Libro("Hábitos Atómicos", "James Clear", 49.90, R.drawable.habitos_atomicos)
        )


        rvLibros.layoutManager = LinearLayoutManager(this)
        rvLibros.adapter = LibroAdapter(libros)

        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
