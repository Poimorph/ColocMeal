package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.RecipeDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO 3.2 : complete remote
class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override fun observeSharedRecipes(houseId: String): Flow<List<Recipe>> =
        recipeDao.getRecipesForHouse(houseId).map { entities -> entities.map { it.toDomain() } }

    override fun observePrivateRecipes(authorId: String): Flow<List<Recipe>> =
        recipeDao.getRecipesByAuthor(authorId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getRecipe(id: String): Recipe? =
        recipeDao.getRecipeById(id)?.toDomain()

    override suspend fun upsertRecipe(recipe: Recipe) {
        recipeDao.upsert(recipe.toEntity())
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.delete(recipe.toEntity())
    }
}
