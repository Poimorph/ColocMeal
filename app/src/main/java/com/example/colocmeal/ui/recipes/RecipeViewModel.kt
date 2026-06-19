package com.example.colocmeal.ui.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.RecipeRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.ui.container
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class RecipeViewModel(
    private val houseId: String,
    private val repository: RecipeRepository,
    authRepository: AuthRepository,
    userRepository: UserRepository,
) : ViewModel() {

    private val uid = authRepository.currentUid.orEmpty()

    private val displayName: StateFlow<String> =
        userRepository.observeUser(uid).map { it?.displayName.orEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val recipes: StateFlow<List<Recipe>> =
        repository.observeSharedRecipes(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addRecipe(name: String, description: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            repository.upsertRecipe(
                Recipe(
                    id = UUID.randomUUID().toString(),
                    name = trimmed,
                    description = description.ifBlank { null },
                    ingredients = emptyList(),
                    authorId = uid,
                    authorName = displayName.value,
                    houseId = houseId,
                    isShared = true,
                )
            )
        }
    }

    fun deleteRecipe(recipeId: String) {
        val recipe = recipes.value.find { it.id == recipeId } ?: return
        viewModelScope.launch { repository.deleteRecipe(recipe) }
    }

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer {
                RecipeViewModel(
                    houseId,
                    container().recipeRepository,
                    container().authRepository,
                    container().userRepository,
                )
            }
        }
    }
}