package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun observeSharedRecipes(houseId: String): Flow<List<Recipe>>

    fun observePrivateRecipes(authorId: String): Flow<List<Recipe>>

    suspend fun getRecipe(id: String): Recipe?

    suspend fun upsertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)
}
