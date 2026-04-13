package com.example.kotlin_libreria.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_libreria.databinding.ItemProductoBinding
import com.example.kotlin_libreria.model.LibroApi
import com.example.kotlin_libreria.model.Producto
import com.squareup.picasso.Picasso

class LibroAdapter(
    private val libros: List<LibroApi>,
    private val onAgregarClick: (Producto) -> Unit
) : RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LibroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        holder.bind(libros[position])
    }

    override fun getItemCount(): Int = libros.size

    inner class LibroViewHolder(
        private val binding: ItemProductoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(libro: LibroApi) {
            binding.tvNombre.text = libro.nombre
            binding.tvCategoria.text = libro.autor
            binding.tvPrecio.text = "S/. ${String.format("%.2f", libro.precio)}"

            if (libro.imagen1.isNotEmpty()) {
                Picasso.get()
                    .load(libro.imagen1)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivProducto)
            }

            binding.btnAgregar.setOnClickListener {
                val producto = Producto(
                    id = libro.id,
                    nombre = libro.nombre,
                    imagen = libro.imagen1,
                    categoria = libro.autor,
                    precio = libro.precio,
                    descripcion = libro.descripcion
                )
                onAgregarClick(producto)
            }
        }
    }
}
