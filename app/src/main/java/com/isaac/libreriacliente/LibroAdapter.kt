package com.isaac.libreriacliente

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LibroAdapter(private val lista: List<Libro>) :
    RecyclerView.Adapter<LibroAdapter.LibroViewHolder>() {

    class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLibro: ImageView = itemView.findViewById(R.id.imgLibro)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvAutor: TextView = itemView.findViewById(R.id.tvAutor)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = lista[position]

        holder.imgLibro.setImageResource(libro.imagenResId)
        holder.tvTitulo.text = libro.titulo
        holder.tvAutor.text = libro.autor
        holder.tvPrecio.text = "S/ %.2f".format(libro.precio)

        // ✅ CLICK EN ITEM
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalleActivity::class.java)
            intent.putExtra("titulo", libro.titulo)
            intent.putExtra("autor", libro.autor)
            intent.putExtra("precio", libro.precio)
            intent.putExtra("imagenResId", libro.imagenResId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = lista.size
}
