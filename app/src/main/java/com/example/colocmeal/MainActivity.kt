package com.example.colocmeal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.colocmeal.ui.navigation.ColocMealNavHost
import com.example.colocmeal.ui.theme.ColocMealTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColocMealTheme { ColocMealNavHost() }
        }
    }
}