package com.lruiz.urbanapp.data

import com.google.gson.annotations.SerializedName
data class whislistDTO(
    val idProducto: Int,
    val nombreProducto: String,
    val precioProducto: Double,
    val imagenProducto: String
)

data class WhisItem(
    @SerializedName("\$values")
    val values: List<whislistDTO>
)

data class WhislistProductoRequest(
    @SerializedName("id_WishList") val idwhis: Int,
    @SerializedName("id_Producto") val idProducto: Int
)

