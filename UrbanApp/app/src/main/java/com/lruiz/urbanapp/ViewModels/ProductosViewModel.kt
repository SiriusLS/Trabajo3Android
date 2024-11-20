package com.lruiz.urbanapp.ViewModels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lruiz.urbanapp.UrbanApi.RetrofitInstance
import com.lruiz.urbanapp.data.ProductoDTO
import com.lruiz.urbanapp.data.ProductoDetalle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class ProductoViewModel : ViewModel() {
    private val productoApiService = RetrofitInstance.apiService

    private val _productos = MutableStateFlow<List<ProductoDTO>>(emptyList())
    val productos: StateFlow<List<ProductoDTO>> = _productos

    private val _producto= MutableStateFlow<List<ProductoDetalle>>(emptyList())
    val producto: StateFlow<List<ProductoDetalle>> =_producto

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getProductos() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { productoApiService.getProductos() }
                _productos.value = response.productos
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
    fun getProducto(id: Int) {
        Log.e("Id obtenida", "$id")
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { productoApiService.getProducto(id) }
                Log.d("Respuesta API", "Respuesta completa: $response") // Log para verificar la respuesta completa


                if (response != null) {
                    // Crear el objeto ProductoDetalle a partir de la respuesta
                    val productoDetalle = ProductoDetalle(
                        idProducto = response.idProducto,
                        nombreProducto = response.nombreProducto,
                        descripcion = response.descripcion,
                        stock = response.stock,
                        precio = response.precio,
                        urlImagen = response.urlImagen
                    )

                    Log.d("ProductoViewModel", "Producto encontrado: $productoDetalle")
                    _producto.value = listOf(productoDetalle)  // Asignar a la lista
                } else {
                    _errorMessage.value = "Producto no encontrado."
                    Log.e("ProductoViewModel", "Producto con ID $id no encontrado")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("ProductoViewModel", "Error al obtener el producto: ${e.message}")
            }
        }
    }


    /*
    fun getProducto(id: Int) {
        Log.e("Id obtenida", "$id")
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { productoApiService.getProducto(id) }
                if (response.producto.isNotEmpty()) {
                    _producto.value = response.producto
                } else {
                    _errorMessage.value = "Producto no encontrado"
                    Log.e("ProductoViewModel", "El producto con id $id no se encontró.")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                Log.e("ProductoViewModel", "Error al obtener el producto: ${e.message}")
            }
        }
    }

     */

}

