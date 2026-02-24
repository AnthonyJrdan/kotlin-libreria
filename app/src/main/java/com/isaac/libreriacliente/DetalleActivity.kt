package com.isaac.libreriacliente

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetalleActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val imgDetalle = findViewById<ImageView>(R.id.imgDetalle)
        val tvTitulo = findViewById<TextView>(R.id.tvTituloDetalle)
        val tvAutor = findViewById<TextView>(R.id.tvAutorDetalle)
        val tvPrecio = findViewById<TextView>(R.id.tvPrecioDetalle)
        val btnComprar = findViewById<Button>(R.id.btnComprar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // Recibir datos desde intent
        val titulo = intent.getStringExtra("titulo") ?: "Sin título"
        val autor = intent.getStringExtra("autor") ?: "Sin autor"
        val precio = intent.getDoubleExtra("precio", 0.0)
        val imagenResId = intent.getIntExtra("imagenResId", R.mipmap.ic_launcher)

        // Pintar en pantalla
        imgDetalle.setImageResource(imagenResId)
        tvTitulo.text = titulo
        tvAutor.text = autor
        tvPrecio.text = "S/ %.2f".format(precio)

        // ✅ Cambiamos el texto del botón (opcional, por si en XML dice "Comprar")
        btnComprar.text = "Agregar al carrito"

        // ✅ Guardar en carrito
        btnComprar.setOnClickListener {
            val libro = Libro(
                titulo = titulo,
                autor = autor,
                precio = precio,
                imagenResId = imagenResId
            )

            CarritoManager.agregar(libro)
            Toast.makeText(this, "Agregado al carrito ✅", Toast.LENGTH_SHORT).show()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}