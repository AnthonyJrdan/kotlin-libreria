package com.example.kotlin_libreria

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_libreria.adapter.CarritoAdapter
import com.example.kotlin_libreria.databinding.FragmentCarritoBinding
import com.example.kotlin_libreria.model.CarritoManager

class CarritoFragment : Fragment() {
    private var _binding: FragmentCarritoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarritoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        actualizarVista()
    }

    override fun onResume() {
        super.onResume()
        actualizarVista()
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(
            CarritoManager.items.toMutableList(),
            onCantidadChange = { productoId, cantidad ->
                CarritoManager.actualizarCantidad(productoId, cantidad)
                actualizarVista()
            },
            onEliminarClick = { productoId ->
                CarritoManager.eliminarProducto(productoId)
                actualizarVista()
            }
        )
        binding.rvCarrito.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCarrito.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnConfirmar.setOnClickListener {
            val intent = Intent(requireContext(), PagoActivity::class.java)
            startActivity(intent)
        }

        binding.btnVaciar.setOnClickListener {
            CarritoManager.limpiarCarrito()
            actualizarVista()
        }
    }

    private fun actualizarVista() {
        val items = CarritoManager.items
        adapter.updateItems(items.toMutableList())

        if (items.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvCarrito.visibility = View.GONE
            binding.btnConfirmar.visibility = View.GONE
            binding.btnVaciar.visibility = View.GONE
            binding.tvCantidad.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvCarrito.visibility = View.VISIBLE
            binding.btnConfirmar.visibility = View.VISIBLE
            binding.btnVaciar.visibility = View.VISIBLE
            binding.tvCantidad.visibility = View.VISIBLE
            binding.tvCantidad.text = "${CarritoManager.getCantidadTotal()} productos"
        }

        binding.tvTotal.text = "Total: S/. ${String.format("%.2f", CarritoManager.getTotal())}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
