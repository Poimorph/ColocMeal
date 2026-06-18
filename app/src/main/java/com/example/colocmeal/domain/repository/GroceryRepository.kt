package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.GroceryItem
import kotlinx.coroutines.flow.Flow

interface GroceryRepository {

    fun startSync(houseId: String)

    fun observeItems(houseId: String): Flow<List<GroceryItem>>

    suspend fun addOrMergeItem(item: GroceryItem)

    suspend fun setChecked(item: GroceryItem, checked: Boolean)

    suspend fun deleteItem(item: GroceryItem)

    suspend fun clearChecked(houseId: String)
}
