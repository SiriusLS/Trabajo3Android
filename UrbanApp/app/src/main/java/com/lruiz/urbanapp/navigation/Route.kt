 package com.lruiz.urbanapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lruiz.urbanapp.screens.ScreenCarrito
import com.lruiz.urbanapp.screens.ScreenMenu
import com.lruiz.urbanapp.screens.ScreenHistorial
import com.lruiz.urbanapp.screens.ScreenWhislist
import com.lruiz.urbanapp.screens.ScreenProducto
import androidx.navigation.NavType
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreens.ScreenMenu.route
    ) {
        composable(route = AppScreens.ScreenCarrito.route) {
            ScreenCarrito(navController)
        }
        composable(route = AppScreens.ScreenWhislist.route) {
            ScreenWhislist(navController)
        }
        composable(route = AppScreens.ScreenHistorial.route) {
            ScreenHistorial(navController)
        }
        composable(route = AppScreens.ScreenMenu.route) {
            ScreenMenu(navController)
        }
        composable(
            route = "${AppScreens.ScreenProducto.route}?customParam={customParam}",
            arguments = listOf(
                navArgument("customParam") {
                    defaultValue = null
                    nullable = true
                    type = NavType.StringType // Cambié de StringType a IntType
                }
            )
        ) { backStackEntry ->
            val customParamString = backStackEntry.arguments?.getString("customParam")
            val customParam = customParamString?.toIntOrNull()

            // Verifica si la conversión fue exitosa
            if (customParam != null) {
                Log.d("TukiEnvio", "ID Producto recibido: $customParam")
                ScreenProducto(navController, customParam)
            } else {
                Log.e("TukiEnvio", "Error al recibir el ID Producto: valor inválido")
                // Maneja el error (por ejemplo, redirige a una pantalla de error)
            }
        }
    }
}
