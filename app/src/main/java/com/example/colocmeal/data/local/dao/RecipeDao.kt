package com.example.colocmeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.colocmeal.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes WHERE houseId = :houseId AND isShared = 1")
    fun getRecipesForHouse(houseId: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE authorId = :authorId AND isShared = 0")
    fun getRecipesByAuthor(authorId: String): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): RecipeEntity?

    @Upsert
    suspend fun upsert(recipe: RecipeEntity)

    @Delete
    suspend fun delete(recipe: RecipeEntity)
}
