package com.example.colocmeal.ui.planning

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.ui.components.buttons.AppButton
import com.example.colocmeal.ui.components.buttons.AppIconButton
import com.example.colocmeal.ui.components.buttons.ButtonStyle
import com.example.colocmeal.ui.components.cards.AppOutlinedCard
import com.example.colocmeal.ui.components.cards.RecipeCard
import com.example.colocmeal.ui.components.navigation.AppTopAppBar
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Composable
fun MealPlanScreen(
    houseId: String,
    viewModel: PlanningViewModel = viewModel(factory = PlanningViewModel.factory(houseId))
) {
    val weeklyPlan by viewModel.weeklyPlan.collectAsState()
    val recipes by viewModel.recipes.collectAsState()

    var showSelectRecipeDialog by remember { mutableStateOf<Int?>(null) }

    val today = LocalDate.now()
    val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    Scaffold(
        topBar = {
            AppTopAppBar(title = "Menu de la semaine")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val days = listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche")
            items(days.zip(1..7)) { (dayName, dayIndex) ->
                val date = monday.plusDays((dayIndex - 1).toLong())
                val isToday = date == today
                val meal = weeklyPlan.find { it.dayOfWeek == dayIndex }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = dayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    MealSlot(
                        recipeName = meal?.recipeName,
                        cookName = meal?.cookName,
                        onClick = { showSelectRecipeDialog = dayIndex },
                        onDelete = { meal?.let { viewModel.deleteMeal(it.id) } }
                    )
                }
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
fun MealSlot(
    recipeName: String?,
    cookName: String?,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    AppOutlinedCard(
        selected = recipeName != null,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipeName ?: "Ajouter un repas",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (recipeName != null) FontWeight.Bold else FontWeight.Normal,
                    color = if (recipeName != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                    maxLines = 2
                )
                if (recipeName != null && !cookName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "par $cookName",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            if (recipeName != null) {
                AppIconButton(
                    icon = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipes) { recipe ->
                        RecipeCard(
                            recipe = recipe,
                            onClick = { onSelect(recipe) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            AppButton(label = "Annuler", onClick = onDismiss, style = ButtonStyle.TEXT)
        }
    )
}