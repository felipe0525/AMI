package com.example.pentalikami


data class ListaDeMercado(
    val nombre: String = "",
    val fecha: String = "",
    val productos: List<Producto> = listOf(),
    val cantidadProductos: String = "",
    val ubicacion: String = ""
)
