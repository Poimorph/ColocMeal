package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.IngredientDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toDto
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.data.remote.IngredientFirestoreDataSource
import com.example.colocmeal.domain.model.Ingredient
import com.example.colocmeal.domain.repository.IngredientRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao,
    private val remote: IngredientFirestoreDataSource,
    private val scope: CoroutineScope
) : IngredientRepository {

    override fun startSync(houseId: String) {
        scope.launch {
            remote.observeIngredients(houseId).collect { dtos ->
                ingredientDao.upsertAll(dtos.map { it.toDomain().toEntity() })
            }
        }
    }

    override fun observeIngredients(houseId: String): Flow<List<Ingredient>> =
        ingredientDao.getIngredientsForHouse(houseId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun findByNormalizedName(houseId: String, nameNormalized: String): Ingredient? =
        ingredientDao.findByNormalizedName(houseId, nameNormalized)?.toDomain()

    override suspend fun upsertIngredient(ingredient: Ingredient) {
        // Dedup by normalized name within the house: reuse the existing id (just refresh the aisle).
        val existing = ingredientDao.findByNormalizedName(ingredient.houseId, ingredient.nameNormalized)
        val toSave = when {
            existing != null -> ingredient.copy(id = existing.id)
            ingredient.id.isBlank() -> ingredient.copy(id = UUID.randomUUID().toString())
            else -> ingredient
        }
        ingredientDao.upsert(toSave.toEntity())
        remote.upsert(toSave.toDto())
    }
}