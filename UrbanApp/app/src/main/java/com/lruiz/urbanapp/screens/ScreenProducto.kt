package com.lruiz.urbanapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.lruiz.urbanapp.ViewModels.CarritoViewModel
import com.lruiz.urbanapp.ViewModels.ProductoViewModel
import com.lruiz.urbanapp.components.BarraNavegacion
import com.lruiz.urbanapp.components.TopBar
import com.lruiz.urbanapp.data.ProductoDetalle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import com.lruiz.urbanapp.ViewModels.whislistViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Favorite


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenProducto(navController: NavHostController, customParam: Int?) {
    Log.d("Tukiqsasa", "ID Producto: ${customParam}")
    val viewModel: ProductoViewModel = viewModel()
    val producto by viewModel.producto.collectAsState(initial = emptyList())  // Lista vacía, nunca será nula
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(customParam) {
        customParam?.let {
            viewModel.getProducto(it)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { TopBar(navController) }) },
        bottomBar = { BottomAppBar { BarraNavegacion(navController) } },
    ) { innerPadding ->

        if (errorMessage != null) {
            Text(
                text = "Error: $errorMessage",
                color = Color.Red,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        } else {
            Log.d("ProductoList", "Contenido de producto: $producto")
            val firstProducto = producto.firstOrNull()  // Uso de firstOrNull para evitar NoSuchElementException
            if (firstProducto != null) {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(producto){ producto ->
                        ProductoItemDetalle(producto)
                    }
                     // Asumiendo que ProductoItemDetalle maneja correctamente null
                }
            } else {
                Text(
                    text = "Producto no encontrado.",
                    modifier = Modifier
                        .padding(innerPadding)
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
  }

@Composable
fun ProductoItemDetalle(producto: ProductoDetalle) {
    val viewModel: whislistViewModel = viewModel()
    val viewModel2: CarritoViewModel = viewModel()
    var selectedQuantity by remember { mutableIntStateOf(1) }

    val whislistProductos by viewModel.WhislistProductos.collectAsState()
    val isInWishlist = remember(whislistProductos) {
        whislistProductos.any { it.idProducto == producto.idProducto }

    }
    LaunchedEffect(Unit) {
        viewModel.getWhislistProductos() // Recargar la wishlist
    }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = " ${producto.nombreProducto}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                IconButton(
                    onClick = {
                        if (isInWishlist) {
                          //  viewModel.deleteFromWishlist(producto.idProducto)
                        } else {
                            viewModel.addWhislistAlCarrito(
                                idwhis = 1,
                                idProducto = producto.idProducto
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isInWishlist) Color.Red else Color.Gray
                    )
                }
            }
            Row {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = producto.urlImagen,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = producto.precio.toString(), // Precio como ejemplo
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                SimpleQuantitySelector(
                    quantity = selectedQuantity,
                    onQuantityChange = { newQuantity -> selectedQuantity = newQuantity }
                )
            }
            Row {
                OutlinedTextField(
                    value = producto.descripcion,
                    onValueChange = {},
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
            Row {
                Button(modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel2.addProductoAlCarrito(
                            idCarrito = 1, // ID del carrito
                            idProducto = producto.idProducto, // ID del producto actual
                            cantidad = selectedQuantity // Por ejemplo, agregar 1 unidad
                        )
                    }
                ) {
                    Text("Añadir al carrito")
                }
            }
        }
}


@Composable
fun SimpleQuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    minQuantity: Int = 1,
    maxQuantity: Int = 100
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(8.dp)
    ) {
        // Botón de disminuir cantidad
        Button(
            onClick = { if (quantity > minQuantity) onQuantityChange(quantity - 1) },
            enabled = quantity > minQuantity
        ) {
            Text("-")
        }

        // Mostrar cantidad actual
        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Botón de aumentar cantidad
        Button(
            onClick = { if (quantity < maxQuantity) onQuantityChange(quantity + 1) },
            enabled = quantity < maxQuantity
        ) {
            Text("+")
        }
    }
}



