package com.lruiz.urbanapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lruiz.urbanapp.navigation.AppScreens
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.lruiz.urbanapp.R
import com.lruiz.urbanapp.ViewModels.ProductoViewModel

@Composable
fun BarraNavegacion(navController: NavController){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == AppScreens.ScreenHistorial.route
            , label ={
                Text(text = "Historial")
            }
            , onClick = {
                navController.navigate(AppScreens.ScreenHistorial.route){
                    popUpTo(AppScreens.ScreenHistorial.route) { inclusive = true }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Usuario",
                    modifier = Modifier
                        .size(30.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color(0xFF0091EA),
                indicatorColor = Color(0xFF00B8D4)
            )
        )
        NavigationBarItem(
            selected = currentRoute == AppScreens.ScreenMenu.route,
            label = {
                Text(text = "Menu")
            },
            onClick = {
                navController.navigate(AppScreens.ScreenMenu.route){
                    popUpTo(AppScreens.ScreenMenu.route) { inclusive = true }
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Usuario",
                    modifier = Modifier
                        .size(30.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color(0xFF00B8D4),
                indicatorColor = Color(0xFF00B8D4)
            )
        )
        NavigationBarItem(
            selected = currentRoute == AppScreens.ScreenWhislist.route,
            label = {
                Text(text ="Whislist")
            },
            onClick = { navController.navigate(AppScreens.ScreenWhislist.route){
                popUpTo(AppScreens.ScreenWhislist.route) { inclusive = true }
            }
                      },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Usuario",
                    modifier = Modifier
                        .size(30.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedTextColor = Color(0xFF00B8D4),
                indicatorColor = Color(0xFF00B8D4)
            )
        )
    }
}

@Composable
fun TopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Usuario",
            modifier = Modifier
                .size(30.dp)
                .clickable { /* Pantalla Usuario/Perfil...Algun dia */ }
        )

        // Ícono del carrito
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito",
            modifier = Modifier
                .size(30.dp)
                .clickable { navController.navigate(AppScreens.ScreenCarrito.route) }
        )


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Barradebusqueda(){
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    SearchBar(query = text,
        modifier = Modifier.fillMaxWidth(),
        onQueryChange ={
            text = it
        } ,
        onSearch =  { active=false },
        active = active,
        onActiveChange ={ active = it },
        placeholder =   {
            Text(text = "Buscar Producto") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = ""
                        } else {
                            active = false
                        }
                    },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar"
                )
            }
        }

    ) {

    }
}
@Composable
fun ListaDeseo ()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Lista de deseos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        // Lista de productos de ejemplo
        val productos = listOf("Zapatillas tuki $10.000 ", "Mochilla tuki $15.000 ", "Audifonos tuki $20.000 ", "Mouse tuki edicion limitada " +
                "$30.000 ")

        productos.forEach { producto ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Gray) // Imagen del producto
                )
                Text(
                    text = producto,
                    modifier = Modifier.padding(start = 16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}



