package com.example.kotlin_libreria.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_libreria.databinding.ItemCarritoBinding
import com.example.kotlin_libreria.model.CarritoItem
import com.squareup.picasso.Picasso

class CarritoAdapter(
    private var items: MutableList<CarritoItem>,
    private val onCantidadChange: (Int, Int) -> Unit,
    private val onEliminarClick: (Int) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemCarritoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CarritoItem>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }

    inner class CarritoViewHolder(
        private val binding: ItemCarritoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem) {
            val producto = item.producto

            binding.tvNombre.text = producto.nombre
            binding.tvPrecio.text = "S/. ${String.format("%.2f", producto.precio)}"
            binding.tvCantidad.text = item.cantidad.toString()
            binding.tvSubtotal.text = "S/. ${String.format("%.2f", producto.precio * item.cantidad)}"

            if (producto.imagen.isNotEmpty()) {
                Picasso.get()
                    .load(producto.imagen)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivProducto)
            }

            binding.btnAumentar.setOnClickListener {
                val nuevaCantidad = item.cantidad + 1
                onCantidadChange(producto.id, nuevaCantidad)
            }

            binding.btnDisminuir.setOnClickListener {
                if (item.cantidad > 1) {
                    val nuevaCantidad = item.cantidad - 1
                    onCantidadChange(producto.id, nuevaCantidad)
                } else {
                    onEliminarClick(producto.id)
                }
            }

            binding.btnEliminar.setOnClickListener {
                onEliminarClick(producto.id)
            }
        }
    }
}
