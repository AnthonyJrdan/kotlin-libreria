package com.isaac.libreriacliente

object CarritoManager {
    private val items = mutableListOf<Libro>()

    fun agregar(libro: Libro) {
        items.add(libro)
    }

    fun obtener(): List<Libro> = items.toList()

    fun vaciar() {
        items.clear()
    }

    fun total(): Double = items.sumOf { it.precio }
}