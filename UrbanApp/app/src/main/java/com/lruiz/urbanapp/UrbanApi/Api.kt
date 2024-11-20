package com.lruiz.urbanapp.UrbanApi

import com.lruiz.urbanapp.data.CarritoProductoRequest
import com.lruiz.urbanapp.data.CartItem
import com.lruiz.urbanapp.data.ProductoApiResponse
import com.lruiz.urbanapp.data.ProductosApiResponse
import com.lruiz.urbanapp.data.WhisItem
import com.lruiz.urbanapp.data.WhislistProductoRequest
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST
interface ApiService {
    @GET("api/productos")
    suspend fun getProductos(): ProductosApiResponse
    @GET("api/productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): ProductoApiResponse
    @GET("api/CarritoProducto/user/{userId}")
    suspend fun getCarritoProducto(
        @Path("userId") userId: Int,
        @Query("carritoID") carritoID: Int
    ): CartItem
    @GET("api/WishList_Producto/user/{Id_Usuario}")
    suspend fun getWhislistProducto(@Path("Id_Usuario") userId: Int
    ): WhisItem
    @DELETE("api/CarritoProducto/{idproducto}/carrito/{idCarrito}")
    suspend fun deleteProductoFromCarrito(
           @Path("idproducto") idProducto: Int,
           @Path("idCarrito") idCarrito: Int
    ): Response<Unit>
           //api/WishList_Producto/{idProducto}/wishlist/{idWishlist}
    @DELETE("api/WishList_Producto/{idProducto}/wishlist/{idWishlist}")
    suspend fun deleteProductoFromLDeseos(
        @Path("idProducto") idProducto: Int,
        @Path("idWishlist") idWishlist: Int
    ): Response<Unit>
    @POST("api/carritoproducto")
    suspend fun addCarritoProducto(@Body carritoProducto: CarritoProductoRequest): Response<Unit>
    @POST("api/wishlist_producto")
    suspend fun addWhislistProducto(@Body WishLists_Producto: WhislistProductoRequest): Response<Unit>
}

object RetrofitInstance {
    private const val BASE_URL = "https://urbanapi.somee.com/"
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
