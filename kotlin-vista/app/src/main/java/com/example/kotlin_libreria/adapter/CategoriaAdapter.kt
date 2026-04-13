package com.example.kotlin_libreria.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_libreria.databinding.ItemCategoriaBinding
import com.example.kotlin_libreria.model.Categoria
import com.squareup.picasso.Picasso

class CategoriaAdapter(
    private val categorias: List<Categoria>,
    private val onCategoriaClick: (Categoria) -> Unit
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val binding = ItemCategoriaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoriaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(categorias[position])
    }

    override fun getItemCount(): Int = categorias.size

    inner class CategoriaViewHolder(
        private val binding: ItemCategoriaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(categoria: Categoria) {
            binding.tvNombreCategoria.text = categoria.nombre
            binding.tvDescripcionCategoria.text = categoria.descripcion

            if (categoria.imagen.isNotEmpty()) {
                Picasso.get()
                    .load(categoria.imagen)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivCategoria)
            }

            binding.root.setOnClickListener {
                onCategoriaClick(categoria)
            }
        }
    }
}
