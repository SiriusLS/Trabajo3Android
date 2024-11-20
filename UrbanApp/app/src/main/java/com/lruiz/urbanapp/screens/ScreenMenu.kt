package com.lruiz.urbanapp.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.lruiz.urbanapp.ViewModels.ProductoViewModel
import com.lruiz.urbanapp.components.BarraNavegacion
import com.lruiz.urbanapp.components.TopBar
import com.lruiz.urbanapp.data.ProductoDTO
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.lruiz.urbanapp.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenMenu(navController: NavHostController) {
    val viewModel: ProductoViewModel = viewModel()
    val productos by viewModel.productos.collectAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getProductos()
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
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                // Confirma que productos es no nulo y tiene el tipo correcto
                items(productos) { producto ->
                    ProductoItem(producto,navController)
                }
            }


        }
    }


}
@Composable
fun ProductoItem(producto: ProductoDTO, navController: NavController) {
    // Crear un item para cada producto en la lista
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = {
                Log.d("Producto", "ID Producto: ${producto.id_Producto}")
                navController.navigate("${AppScreens.ScreenProducto.route}?customParam=${producto.id_Producto}")

            })
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = producto.nombre_Producto, fontWeight = FontWeight.Bold)
            AsyncImage(
                model = producto.url_Imagen,
                contentDescription = null,
                contentScale = ContentScale.Crop // Esto asegura que la imagen se recorte de manera proporcional
            )
            Text(text = "Precio: \$${producto.precio}")
            Text(text = "Stock: ${producto.stock}")
        }
    }
}

