package com.example.colocmeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.colocmeal.data.local.entity.GroceryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryItemDao {

    @Query("SELECT * FROM grocery_items WHERE houseId = :houseId")
    fun getItemsForHouse(houseId: String): Flow<List<GroceryItemEntity>>

    @Query("SELECT * FROM grocery_items WHERE houseId = :houseId AND nameNormalized = :nameNormalized LIMIT 1")
    suspend fun findByNormalizedName(houseId: String, nameNormalized: String): GroceryItemEntity?

    @Upsert
    suspend fun upsert(item: GroceryItemEntity)

    @Query("UPDATE grocery_items SET isChecked = :isChecked WHERE id = :id")
    suspend fun setChecked(id: String, isChecked: Boolean)

    @Delete
    suspend fun delete(item: GroceryItemEntity)

    @Query("DELETE FROM grocery_items WHERE houseId = :houseId AND isChecked = 1")
    suspend fun deleteCheckedForHouse(houseId: String)
}
