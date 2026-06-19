package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {

    fun startSync(houseId: String)

    fun observeIngredients(houseId: String): Flow<List<Ingredient>>

    suspend fun findByNormalizedName(houseId: String, nameNormalized: String): Ingredient?

    suspend fun upsertIngredient(ingredient: Ingredient)
}