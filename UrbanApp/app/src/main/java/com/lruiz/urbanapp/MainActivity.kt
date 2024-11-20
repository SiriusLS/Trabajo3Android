package com.lruiz.urbanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lruiz.urbanapp.navigation.AppNavigation
import com.lruiz.urbanapp.ui.theme.UrbanAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrbanAppTheme {
                AppNavigation()

            }
        }
    }
}

