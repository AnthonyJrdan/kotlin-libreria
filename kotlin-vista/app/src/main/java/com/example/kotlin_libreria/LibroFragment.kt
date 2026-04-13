package com.example.kotlin_libreria

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_libreria.adapter.CategoriaAdapter
import com.example.kotlin_libreria.adapter.LibroAdapter
import com.example.kotlin_libreria.api.RetrofitClient
import com.example.kotlin_libreria.databinding.FragmentLibroBinding
import com.example.kotlin_libreria.model.CarritoManager
import com.example.kotlin_libreria.model.Categoria
import com.example.kotlin_libreria.model.LibroApi
import com.example.kotlin_libreria.model.Producto
import com.example.kotlin_libreria.model.SessionManager
import kotlinx.coroutines.launch

class LibroFragment : Fragment() {
    private var _binding: FragmentLibroBinding? = null
    private val binding get() = _binding!!

    private var categoriaActual: Int? = null
    private var categoriasCache: List<Categoria>? = null
    private var librosCache: MutableMap<Int, List<LibroApi>> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = "Categorías"
        binding.btnVolver.visibility = View.GONE
        cargarCategorias()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnVolver.setOnClickListener {
            categoriaActual = null
            binding.tvTitle.text = "Categorías"
            binding.btnVolver.visibility = View.GONE
            cargarCategorias()
        }
    }

    private fun cargarCategorias() {
        categoriasCache?.let { cached ->
            mostrarCategorias(cached)
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.categoriaApi.getCategorias()
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body() != null) {
                    val categorias = response.body()!!
                    categoriasCache = categorias
                    mostrarCategorias(categorias)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar categorías", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarCategorias(categorias: List<Categoria>) {
        binding.rvLibros.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLibros.adapter = CategoriaAdapter(categorias) { categoria ->
            if (!SessionManager.isLoggedIn) {
                Toast.makeText(
                    requireContext(),
                    "Inicia sesión para ver los productos",
                    Toast.LENGTH_SHORT
                ).show()
                return@CategoriaAdapter
            }
            categoriaActual = categoria.id
            binding.tvTitle.text = categoria.nombre
            binding.btnVolver.visibility = View.VISIBLE
            cargarLibrosPorCategoria(categoria.id)
        }
    }

    private fun cargarLibrosPorCategoria(categoriaId: Int) {
        librosCache[categoriaId]?.let { cached ->
            mostrarLibros(cached)
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.rvLibros.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.categoriaApi.getLibrosPorCategoria(categoriaId)
                binding.progressBar.visibility = View.GONE
                binding.rvLibros.visibility = View.VISIBLE

                if (response.isSuccessful && response.body() != null) {
                    val libros = response.body()!!
                    librosCache[categoriaId] = libros
                    mostrarLibros(libros)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar libros", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.rvLibros.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarLibros(libros: List<LibroApi>) {
        binding.rvLibros.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLibros.adapter = LibroAdapter(libros) { producto ->
            agregarAlCarrito(producto)
        }

        if (libros.isEmpty()) {
            Toast.makeText(requireContext(), "No hay libros en esta categoría", Toast.LENGTH_SHORT).show()
        }
    }

    private fun agregarAlCarrito(producto: Producto) {
        if (!SessionManager.isLoggedIn) {
            Toast.makeText(
                requireContext(),
                "Inicia sesión para agregar productos al carrito",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return
        }
        CarritoManager.agregarProducto(producto)
        Toast.makeText(
            requireContext(),
            "${producto.nombre} agregado al carrito",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
