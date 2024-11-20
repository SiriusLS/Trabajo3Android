package com.lruiz.urbanapp.screens
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lruiz.urbanapp.ViewModels.CarritoViewModel
import com.lruiz.urbanapp.components.BarraNavegacion
import com.lruiz.urbanapp.data.carritoDTO


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCarrito(navController: NavHostController) {
    val viewModel: CarritoViewModel = viewModel()
    val carrito by viewModel.carritoProductos.collectAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCarritoProductos()
    }
    Scaffold(
        topBar = { TopAppBar( title = { Text(text = "CARRITO")} ) },
        bottomBar = { BottomAppBar (modifier = Modifier){
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Continuar Compra")
            }
            BarraNavegacion(navController)
        }
        }){ innerPadding ->
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
                items(carrito) { producto ->
                    ProductRow(producto,navController)
                }
            }


        }
    }
}

@Composable
fun ProductRow(carrito: carritoDTO, navController: NavController) {
    val viewModel: CarritoViewModel = viewModel()
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        // casilla box
        Box(
            modifier = Modifier
                .size(28.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = carrito.cantidad.toString(),  // cantidad
                fontSize = 15.sp
            )
        }

        // entre la casilla y el nombre del producto
        Spacer(modifier = Modifier.width(8.dp))

        // Nombre del producto
        Text(
            text = carrito.nombreProducto,
            fontSize = 25.sp,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = {
            showDialog = true
        }) {
            Icon(Icons.Default.Clear, contentDescription = "Menu")
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Eliminar Producto") },
                text = { Text("¿Estás seguro de que deseas eliminar este producto del carrito?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Llama a la función para eliminar el producto
                            viewModel.deleteProductoFromCarrito(carrito.idProducto, 1)
                            showDialog = false
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
    }
}
