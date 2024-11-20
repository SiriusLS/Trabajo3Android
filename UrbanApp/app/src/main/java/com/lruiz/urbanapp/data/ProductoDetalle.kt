package com.lruiz.urbanapp.data

import com.google.gson.annotations.SerializedName

data class ProductosApiResponse(
    @SerializedName("\$values")
    val productos: List<ProductoDTO>
)

data class ProductoDTO(
    val id_Producto: Int,
    val nombre_Producto: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val url_Imagen: String
)

data class ProductoApiResponse(
    @SerializedName("id_Producto") val idProducto: Int,
    @SerializedName("nombre_Producto") val nombreProducto: String,
    val descripcion: String,
    val stock: Int,
    val precio: Double,
    @SerializedName("url_Imagen") val urlImagen: String
)

data class ProductoDetalle(
    val idProducto: Int,
    val nombreProducto: String,
    val descripcion: String,
    val stock: Int,
    val precio: Double,
    val urlImagen: String
)