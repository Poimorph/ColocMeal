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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.ui.components.navigation.AppNavigationBar
import com.example.colocmeal.ui.components.navigation.NavDestination
import com.example.colocmeal.ui.grocery.GroceryScreen
import com.example.colocmeal.ui.house.HouseDetailScreen
import com.example.colocmeal.ui.planning.MealPlanScreen
import com.example.colocmeal.ui.recipes.RecipeScreen

@Composable
fun HomeScreen(
    houseId: String,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(houseId)),
) {
    val house by viewModel.house.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()

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
        Box(Modifier
            .fillMaxSize()
            .padding(padding)) {
            when (current) {
                "planning" -> MealPlanScreen(houseId = houseId)
                "recipes" -> RecipeScreen(houseId = houseId)
                "grocery" -> GroceryScreen(houseId = houseId, house = house)
                "house" -> HouseDetailScreen(
                    house = house,
                    members = members,
                    onQuitHouse = viewModel::quitHouse,
                    onDisconnect = viewModel::disconnect,
                )
            }
        }
    }
}