package com.example.colocmeal.ui.recipes

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.Ingredient
import com.example.colocmeal.ui.components.grocery.AisleSelector
import com.example.colocmeal.ui.components.grocery.AppChip
import com.example.colocmeal.ui.components.buttons.AppSwitch
import com.example.colocmeal.ui.components.navigation.AppTopAppBar
import com.example.colocmeal.ui.components.grocery.ChipKind
import com.example.colocmeal.ui.components.buttons.AppButton
import com.example.colocmeal.ui.components.buttons.AppFab
import com.example.colocmeal.ui.components.buttons.AppIconButton
import com.example.colocmeal.ui.components.buttons.ButtonStyle
import com.example.colocmeal.ui.components.cards.RecipeCard
import com.example.colocmeal.ui.components.inputs.AppTextField

@Composable
fun RecipeScreen(
    houseId: String,
    viewModel: RecipeViewModel = viewModel(factory = RecipeViewModel.factory(houseId))
) {
    val recipes by viewModel.recipes.collectAsState()
    val catalog by viewModel.catalog.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { AppTopAppBar(title = "Recettes de la coloc") },
        floatingActionButton = {
            AppFab(
                icon = Icons.Default.Add,
                contentDescription = "Ajouter une recette",
                onClick = { showAddDialog = true }
            )
        }
    ) { padding ->
        if (recipes.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Aucune recette pour le moment.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recipes) { recipe ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RecipeCard(
                            recipe = recipe,
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        )
                        AppIconButton(
                            icon = Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            onClick = { viewModel.deleteRecipe(recipe.id) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AddRecipeDialog(
                catalog = catalog,
                onCreateIngredient = viewModel::createIngredient,
                onDismiss = { showAddDialog = false },
                onConfirm = { name, description, ingredients, isShared ->
                    viewModel.addRecipe(name, description, ingredients, isShared)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AddRecipeDialog(
    catalog: List<Ingredient>,
    onCreateIngredient: (name: String, aisle: Aisle) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, ingredients: List<String>, isShared: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var ingredientDraft by remember { mutableStateOf("") }
    var draftAisle by remember { mutableStateOf(Aisle.OTHER) }
    val ingredients = remember { mutableStateListOf<String>() }
    var isShared by remember { mutableStateOf(true) }

    fun addSelected(value: String) {
        if (ingredients.none { it.equals(value, ignoreCase = true) }) ingredients.add(value)
    }

    fun commitDraft() {
        val value = ingredientDraft.trim()
        if (value.isNotEmpty()) {
            onCreateIngredient(value, draftAisle) // register / refresh aisle in the house catalog
            addSelected(value)
        }
        ingredientDraft = ""
        draftAisle = Aisle.OTHER
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nouvelle recette") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                AppTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Nom de la recette",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                AppTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description/Instructions",
                    modifier = Modifier.fillMaxWidth()
                )

                // --- Selected ingredients ---
                if (ingredients.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        ingredients.forEachIndexed { index, ingredient ->
                            AppChip(
                                label = ingredient,
                                kind = ChipKind.INPUT,
                                onDismiss = { ingredients.removeAt(index) }
                            )
                        }
                    }
                }

                // --- Add a new ingredient (name + aisle) ---
                Spacer(modifier = Modifier.height(12.dp))
                Text("Ajouter un ingrédient", style = MaterialTheme.typography.labelMedium)
                AppTextField(
                    value = ingredientDraft,
                    onValueChange = { ingredientDraft = it },
                    label = "Nom de l'ingrédient",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                AisleSelector(
                    selected = draftAisle,
                    onSelect = { draftAisle = it }
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppButton(
                    label = "Ajouter l'ingrédient",
                    onClick = { commitDraft() },
                    style = ButtonStyle.TONAL,
                    enabled = ingredientDraft.isNotBlank()
                )

                // --- Import an existing catalog ingredient ---
                val importable = catalog.filter { ing -> ingredients.none { it.equals(ing.name, ignoreCase = true) } }
                if (importable.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Ingrédients existants", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        importable.forEach { ing ->
                            AppChip(
                                label = "${ing.aisle.emoji} ${ing.name}",
                                kind = ChipKind.ASSIST,
                                onClick = { addSelected(ing.name) }
                            )
                        }
                    }
                }

                // --- Share with house / private ---
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Partager avec la coloc")
                        Text(
                            text = if (isShared) "Visible par tous les colocataires" else "Recette privée (visible par vous seul)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    AppSwitch(checked = isShared, onCheckedChange = { isShared = it })
                }
            }
        },
        confirmButton = {
            AppButton(
                label = "Ajouter",
                onClick = {
                    // fold any uncommitted ingredient draft into the recipe + catalog before saving
                    val finalIngredients = ingredients.toMutableList()
                    ingredientDraft.trim().takeIf { it.isNotEmpty() }?.let { draft ->
                        onCreateIngredient(draft, draftAisle)
                        if (finalIngredients.none { existing -> existing.equals(draft, ignoreCase = true) }) {
                            finalIngredients.add(draft)
                        }
                    }
                    onConfirm(name, description, finalIngredients, isShared)
                },
                enabled = name.isNotBlank()
            )
        },
        dismissButton = {
            AppButton(label = "Annuler", onClick = onDismiss, style = ButtonStyle.TEXT)
        }
    )
}