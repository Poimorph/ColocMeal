package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.RecipeDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toDto
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.data.remote.RecipeFirestoreDataSource
import com.example.colocmeal.di.FirebaseProvider
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao,
    private val remote: RecipeFirestoreDataSource,
    private val scope: CoroutineScope
) : RecipeRepository {

    fun startSync(houseId: String, authorId: String) {
        scope.launch {
            remote.observeSharedRecipes(houseId).collect { dtos ->
                recipeDao.upsertAll(dtos.map { it.toDomain().toEntity() })
            }
        }
        scope.launch {
            remote.observePrivateRecipes(authorId).collect { dtos ->
                recipeDao.upsertAll(dtos.map { it.toDomain().toEntity() })
            }
        }
    }

    override fun observeSharedRecipes(houseId: String): Flow<List<Recipe>> =
        recipeDao.getRecipesForHouse(houseId).map { entities -> entities.map { it.toDomain() } }

    override fun observePrivateRecipes(authorId: String): Flow<List<Recipe>> =
        recipeDao.getRecipesByAuthor(authorId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getRecipe(id: String): Recipe? =
        recipeDao.getRecipeById(id)?.toDomain()

    override suspend fun upsertRecipe(recipe: Recipe) {
        remote.upsert(recipe.toDto())
        recipeDao.upsert(recipe.toEntity())
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        remote.delete(recipe.id)
        recipeDao.delete(recipe.toEntity())
    }
}
