package com.example.colocmeal.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.colocmeal.data.local.entity.IngredientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {

    @Query("SELECT * FROM ingredients WHERE houseId = :houseId ORDER BY name COLLATE NOCASE ASC")
    fun getIngredientsForHouse(houseId: String): Flow<List<IngredientEntity>>

    @Query("SELECT * FROM ingredients WHERE houseId = :houseId AND nameNormalized = :nameNormalized LIMIT 1")
    suspend fun findByNormalizedName(houseId: String, nameNormalized: String): IngredientEntity?

    @Upsert
    suspend fun upsert(ingredient: IngredientEntity)

    @Upsert
    suspend fun upsertAll(ingredients: List<IngredientEntity>)
}