package com.lruiz.urbanapp.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lruiz.urbanapp.UrbanApi.RetrofitInstance
import com.lruiz.urbanapp.UrbanApi.RetrofitInstance.apiService
import com.lruiz.urbanapp.data.WhislistProductoRequest
import com.lruiz.urbanapp.data.whislistDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class whislistViewModel() : ViewModel() {
    private val whislistApiService = RetrofitInstance.apiService

    private val _WhislistProductos = MutableStateFlow<List<whislistDTO>>(emptyList())
    val WhislistProductos: StateFlow<List<whislistDTO>> = _WhislistProductos

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun isProductInWishlist(productId: Int): Boolean {
        return _WhislistProductos.value.any { it.idProducto == productId }
    }


    fun getWhislistProductos() {
        viewModelScope.launch {
            try {
                val response =
                    withContext(Dispatchers.IO) { whislistApiService.getWhislistProducto(1) }
                _WhislistProductos.value = response.values
            } catch (e: Exception) {
                // En caso de error, asignamos el mensaje de error al LiveData
                _errorMessage.value = e.message
                Log.e(
                    "whislistViewModel",
                    "Error al obtener los productos del carrito: ${e.message}"
                )
            }
        }
    }
    fun addWhislistAlCarrito(idwhis: Int, idProducto: Int) {
        viewModelScope.launch {
            try {
                val request = WhislistProductoRequest(
                    idwhis = idwhis,
                    idProducto = idProducto,
                )
                val response = withContext(Dispatchers.IO) {
                    whislistApiService.addWhislistProducto(request)
                }
                if (response.isSuccessful) {
                    Log.d("whislistViewModel", "Producto agregado correctamente")
                    // Si es necesario, puedes recargar el carrito después del POST
                    getWhislistProductos()
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = "Error al agregar producto: $idwhis"
                    Log.d("whislistViewModel", "Enviando solicitud con idwhis: $idwhis, idProducto: $idProducto")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.message}"
                Log.e("whislistViewModel1", "Error al agregar producto", e)
            }
        }
    }

    private val _successMessage = MutableStateFlow<Boolean>(false)
    val successMessage: StateFlow<Boolean> = _successMessage

    fun deleteProductoFromWis(idProducto: Int, idWishlist: Int) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.deleteProductoFromLDeseos(idProducto, idWishlist)
                }
                if (response.isSuccessful) {
                    _successMessage.value = true
                    Log.d("WisViewModel", "Producto eliminado del fav correctamente")
                    getWhislistProductos()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    _errorMessage.value = errorBody
                    Log.e("WisViewModel", "Error al eliminar producto del fav: $idProducto $idWishlist ${_errorMessage.value}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Excepción: ${e.message ?: "Error desconocido"}"
                Log.e("WisViewModel", "No funcionó", e)
            }
        }
    }

}