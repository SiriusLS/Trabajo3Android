package com.lruiz.urbanapp.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lruiz.urbanapp.UrbanApi.RetrofitInstance
import com.lruiz.urbanapp.UrbanApi.RetrofitInstance.apiService
import com.lruiz.urbanapp.data.CarritoProductoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.lruiz.urbanapp.data.carritoDTO
import kotlin.Int

class CarritoViewModel() : ViewModel() {
    private val carritoApiService = RetrofitInstance.apiService

    private val _carritoProductos = MutableStateFlow<List<carritoDTO>>(emptyList())
    val carritoProductos: StateFlow<List<carritoDTO>>  = _carritoProductos

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun getCarritoProductos() {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { carritoApiService.getCarritoProducto(1,1) }
                _carritoProductos.value=response.values
            } catch (e: Exception) {
                // En caso de error, asignamos el mensaje de error al LiveData
                _errorMessage.value = e.message
                Log.e("CarritoViewModel", "Error al obtener los productos del carrito: ${e.message}")
            }
        }
    }

    fun addProductoAlCarrito(idCarrito: Int, idProducto: Int, cantidad: Int) {
        viewModelScope.launch {
            try {
                val request = CarritoProductoRequest(
                    idCarrito = idCarrito,
                    idProducto = idProducto,
                    cantidad = cantidad
                )
                val response = withContext(Dispatchers.IO) {
                    carritoApiService.addCarritoProducto(request)
                }
                if (response.isSuccessful) {
                    Log.d("CarritoViewModel", "Producto agregado correctamente")
                    // Si es necesario, puedes recargar el carrito después del POST
                    getCarritoProductos()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al agregar producto: $errorBody"
                    Log.e("CarritoViewModel", "Error al agregar producto: $errorBody")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.message}"
                Log.e("CarritoViewModel", "Error al agregar producto", e)
            }
        }
    }
    private val _successMessage = MutableStateFlow<Boolean>(false)
    val successMessage: StateFlow<Boolean> = _successMessage

    fun deleteProductoFromCarrito(idProducto: Int, idCarrito: Int) {
        viewModelScope.launch {
            try {
                // Realizamos la solicitud DELETE para eliminar el producto del carrito
                val response = withContext(Dispatchers.IO) {
                    apiService.deleteProductoFromCarrito(idProducto, idCarrito)
                }

                if (response.isSuccessful) {
                    _successMessage.value = true
                    Log.d("CarritoViewModel", "Producto eliminado del carrito correctamente")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al eliminar producto del carrito: $errorBody"
                    Log.e("CarritoViewModel", "Error al eliminar producto del carrito: $errorBody")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.message}"
                Log.e("CarritoViewModel", "Error al eliminar producto del carrito", e)
            }
        }
    }


}

