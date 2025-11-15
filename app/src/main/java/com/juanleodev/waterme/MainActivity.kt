package com.juanleodev.waterme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.juanleodev.waterme.ui.navigation.AppNavigation
import com.juanleodev.waterme.ui.theme.WaterMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WaterMeTheme {
                AppNavigation()
            }
        }
    }
}