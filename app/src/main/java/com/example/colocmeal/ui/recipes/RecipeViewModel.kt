package com.example.colocmeal.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.Ingredient
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.IngredientRepository
import com.example.colocmeal.domain.repository.RecipeRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.domain.utils.normalizeName
import com.example.colocmeal.ui.container
import com.example.colocmeal.ui.launchSafe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class RecipeViewModel(
    private val houseId: String,
    private val repository: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    authRepository: AuthRepository,
    userRepository: UserRepository,
) : ViewModel() {

    private val uid = authRepository.currentUid.orEmpty()

    private val displayName: StateFlow<String> =
        userRepository.observeUser(uid).map { it?.displayName.orEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val recipes: StateFlow<List<Recipe>> =
        combine(
            repository.observeSharedRecipes(houseId),
            repository.observePrivateRecipes(uid)
        ) { shared, mine -> (shared + mine).distinctBy { it.id } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** House-wide ingredient catalog (name + aisle), for the recipe ingredient picker. */
    val catalog: StateFlow<List<Ingredient>> =
        ingredientRepository.observeIngredients(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Registers a new ingredient (or refreshes its aisle) in the house catalog. */
    fun createIngredient(name: String, aisle: Aisle) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        launchSafe {
            ingredientRepository.upsertIngredient(
                Ingredient(
                    id = "",
                    houseId = houseId,
                    name = trimmed,
                    nameNormalized = normalizeName(trimmed),
                    aisle = aisle,
                )
            )
        }
    }

    fun addRecipe(name: String, description: String, ingredients: List<String>, isShared: Boolean) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        val cleanIngredients = ingredients.map { it.trim() }.filter { it.isNotEmpty() }
        launchSafe {
            repository.upsertRecipe(
                Recipe(
                    id = UUID.randomUUID().toString(),
                    name = trimmed,
                    description = description.ifBlank { null },
                    ingredients = cleanIngredients,
                    authorId = uid,
                    authorName = displayName.value,
                    houseId = if (isShared) houseId else null,
                    isShared = isShared,
                )
            )
        }
    }

    fun deleteRecipe(recipeId: String) {
        val recipe = recipes.value.find { it.id == recipeId } ?: return
        launchSafe { repository.deleteRecipe(recipe) }
    }

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer {
                RecipeViewModel(
                    houseId,
                    container().recipeRepository,
                    container().ingredientRepository,
                    container().authRepository,
                    container().userRepository,
                )
            }
        }
    }
}