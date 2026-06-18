package com.example.colocmeal.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.ui.components.AppNavigationBar
import com.example.colocmeal.ui.components.AppTopAppBar
import com.example.colocmeal.ui.components.NavDestination

@Composable
fun HomeScreen(
    houseId: String,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(houseId))
) {
    val house by viewModel.house.collectAsStateWithLifecycle()

    val destinations = listOf(
        NavDestination("planning", "Planning", Icons.Filled.DateRange),
        NavDestination("recipes", "Recipes", Icons.AutoMirrored.Filled.List),
        NavDestination("grocery", "Grocery", Icons.Filled.ShoppingCart),
        NavDestination("house", "House", Icons.Filled.Home),
    )
    var current by remember { mutableStateOf(destinations.first().route) }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = house?.name ?: "My House",
                subtitle = house?.inviteCode?.let { "Invite code: $it" }
            )
        },
        bottomBar = {
            AppNavigationBar(
                destinations = destinations,
                currentRoute = current,
                onSelect = { current = it.route }
            )
        }
    ) { padding ->
        //placeholder
        Box(
            Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            val label = destinations.first { it.route == current }.label
            Text("$label — coming soon")
        }
    }
}