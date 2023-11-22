package com.example.pentalikami


data class Producto(
    val nombre: String = "",
    val contenido: String = "",
    val tipo: String = "",
    var unidades: Int = 0,
    var imagenRuta: String = ""
)
