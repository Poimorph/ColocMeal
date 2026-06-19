package com.example.colocmeal.ui.planning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(
    houseId: String,
    viewModel: PlanningViewModel = viewModel(factory = PlanningViewModel.factory(houseId))
) {
    val weeklyPlan by viewModel.weeklyPlan.collectAsState()
    val recipes by viewModel.recipes.collectAsState()

    var showSelectRecipeDialog by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menu de la semaine") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val days = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")
            items(days.zip(1..7)) { (dayName, dayIndex) ->
                val meal = weeklyPlan.find { it.dayOfWeek == dayIndex }
                MealPlanDayRow(
                    dayName = dayName,
                    meal = meal,
                    onAdd = { showSelectRecipeDialog = dayIndex },
                    onDelete = { meal?.let { viewModel.deleteMeal(it.id) } }
                )
                HorizontalDivider()
            }
        }

        if (showSelectRecipeDialog != null) {
            SelectRecipeDialog(
                recipes = recipes,
                onDismiss = { showSelectRecipeDialog = null },
                onSelect = { recipe ->
                    viewModel.addMeal(showSelectRecipeDialog!!, recipe)
                    showSelectRecipeDialog = null
                }
            )
        }
    }
}

@Composable
fun MealPlanDayRow(
    dayName: String,
    meal: MealPlan?,
    onAdd: () -> Unit,
    onDelete: () -> Unit
) {
    ListItem(
        headlineContent = { Text(dayName) },
        supportingContent = {
            Text(meal?.recipeName ?: "Pas de repas prévu")
        },
        trailingContent = {
            if (meal != null) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                }
            } else {
                IconButton(onClick = onAdd) {
                    Icon(Icons.Default.Add, contentDescription = "Prévoir")
                }
            }
        }
    )
}

@Composable
fun SelectRecipeDialog(
    recipes: List<Recipe>,
    onDismiss: () -> Unit,
    onSelect: (Recipe) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choisir une recette") },
        text = {
            if (recipes.isEmpty()) {
                Text("Aucune recette disponible. Ajoutez-en d'abord dans l'onglet Recettes.")
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp)) {
                    items(recipes) { recipe ->
                        ListItem(
                            headlineContent = { Text(recipe.name) },
                            modifier = Modifier.clickable { onSelect(recipe) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Annuler") }
        }
    )
}