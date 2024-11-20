package com.lruiz.urbanapp.navigation

sealed class AppScreens(val route: String){
    object ScreenMenu:AppScreens("ScreenMenu")
    object ScreenCarrito:AppScreens("ScreenCarrito")
    object ScreenWhislist: AppScreens("ScreenWhislist")
    object ScreenHistorial: AppScreens("ScreenHistorial")
    object ScreenProducto: AppScreens("ScreenProducto")
}