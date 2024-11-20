package com.lruiz.urbanapp.data

import com.google.gson.annotations.SerializedName

data class carritoDTO(
    val idProducto: Int,
    val nombreProducto: String,
    val precioProducto: Double,
    val cantidad: Int,
    val imagenProducto: String
)

data class CartItem(
    @SerializedName("\$values")
    val values: List<carritoDTO>
)


data class CarritoProductoRequest(
    @SerializedName("id_Carrito") val idCarrito: Int,
    @SerializedName("id_Producto") val idProducto: Int,
    @SerializedName("cantidad") val cantidad: Int
)

