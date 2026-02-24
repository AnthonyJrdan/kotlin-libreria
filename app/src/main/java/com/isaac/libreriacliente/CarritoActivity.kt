package com.isaac.libreriacliente

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CarritoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        val rvCarrito = findViewById<RecyclerView>(R.id.rvCarrito)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val btnVaciar = findViewById<Button>(R.id.btnVaciar)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        rvCarrito.layoutManager = LinearLayoutManager(this)
        rvCarrito.adapter = LibroAdapter(CarritoManager.obtener()) // reutilizamos tu adapter

        tvTotal.text = "Total: S/ %.2f".format(CarritoManager.total())

        btnVaciar.setOnClickListener {
            CarritoManager.vaciar()
            rvCarrito.adapter = LibroAdapter(CarritoManager.obtener())
            tvTotal.text = "Total: S/ %.2f".format(CarritoManager.total())
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}