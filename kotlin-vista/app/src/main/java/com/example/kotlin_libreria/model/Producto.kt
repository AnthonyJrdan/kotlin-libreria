package com.example.kotlin_libreria.model

data class Producto(
    val id: Int,
    val nombre: String,
    val imagen: String,
    val categoria: String,
    val precio: Double,
    val descripcion: String = ""
)

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int = 1
)

object CarritoManager {
    private val _items = mutableListOf<CarritoItem>()
    val items: List<CarritoItem> get() = _items.toList()

    fun agregarProducto(producto: Producto) {
        val existente = _items.find { it.producto.id == producto.id }
        if (existente != null) {
            existente.cantidad++
        } else {
            _items.add(CarritoItem(producto, 1))
        }
    }

    fun eliminarProducto(productoId: Int) {
        _items.removeAll { it.producto.id == productoId }
    }

    fun actualizarCantidad(productoId: Int, cantidad: Int) {
        val item = _items.find { it.producto.id == productoId }
        if (item != null) {
            if (cantidad <= 0) {
                eliminarProducto(productoId)
            } else {
                item.cantidad = cantidad
            }
        }
    }

    fun limpiarCarrito() {
        _items.clear()
    }

    fun getTotal(): Double {
        return _items.sumOf { it.producto.precio * it.cantidad }
    }

    fun getCantidadTotal(): Int {
        return _items.sumOf { it.cantidad }
    }
}
