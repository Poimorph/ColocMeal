package com.example.colocmeal.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.ui.components.buttons.AppButton
import com.example.colocmeal.ui.components.buttons.ButtonStyle
import com.example.colocmeal.ui.components.navigation.AppNavigationBar
import com.example.colocmeal.ui.components.navigation.AppTopAppBar
import com.example.colocmeal.ui.components.navigation.NavDestination
import com.example.colocmeal.ui.grocery.GroceryScreen
import com.example.colocmeal.ui.planning.MealPlanScreen
import com.example.colocmeal.ui.recipes.RecipeScreen

@Composable
fun HomeScreen(
    houseId: String,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(houseId)),
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
        bottomBar = {
            AppNavigationBar(
                destinations = destinations,
                currentRoute = current,
                onSelect = { current = it.route }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (current) {
                "planning" -> MealPlanScreen(houseId = houseId)
                "recipes" -> RecipeScreen(houseId = houseId)
                "grocery" -> GroceryScreen(houseId = houseId, house = house)
                "house" -> HouseTab(
                    house = house,
                    onQuitHouse = viewModel::quitHouse,
                    onDisconnect = viewModel::disconnect,
                )
            }
        }
    }
}

@Composable
private fun HouseTab(
    house: House?,
    onQuitHouse: () -> Unit,
    onDisconnect: () -> Unit,
) {
    var showQuitConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = house?.name ?: "My House",
                subtitle = house?.inviteCode?.let { "Invite code: $it" }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        ) {
            Text(house?.let { "Invite code: ${it.inviteCode}" } ?: "Loading…")
            AppButton(
                label = "Quit House",
                onClick = { showQuitConfirm = true },
                style = ButtonStyle.OUTLINED,
                enabled = house != null,
            )
            AppButton(
                label = "Disconnect",
                onClick = onDisconnect,
                style = ButtonStyle.TONAL,
            )
        }
    }

    if (showQuitConfirm) {
        AlertDialog(
            onDismissRequest = { showQuitConfirm = false },
            title = { Text("Quit house?") },
            text = { Text("You'll leave \"${house?.name ?: "this house"}\" and need an invite code to rejoin.") },
            confirmButton = {
                TextButton(onClick = {
                    showQuitConfirm = false
                    onQuitHouse()
                }) { Text("Quit") }
            },
            dismissButton = {
                TextButton(onClick = { showQuitConfirm = false }) { Text("Cancel") }
            }
        )
    }
}