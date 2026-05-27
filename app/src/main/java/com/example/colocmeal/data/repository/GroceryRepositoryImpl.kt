package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.GroceryItemDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.repository.GroceryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class GroceryRepositoryImpl(
    private val groceryItemDao: GroceryItemDao
) : GroceryRepository {

    override fun observeItems(houseId: String): Flow<List<GroceryItem>> =
        groceryItemDao.getItemsForHouse(houseId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun addOrMergeItem(item: GroceryItem) {
        val existing = groceryItemDao.findByNormalizedName(item.houseId, item.nameNormalized)
        if (existing != null) return
        val toInsert = if (item.id.isBlank()) item.copy(id = UUID.randomUUID().toString()) else item
        groceryItemDao.upsert(toInsert.toEntity())
    }

    override suspend fun setChecked(item: GroceryItem, checked: Boolean) {
        groceryItemDao.setChecked(item.id, checked)
    }

    override suspend fun deleteItem(item: GroceryItem) {
        groceryItemDao.delete(item.toEntity())
    }

    override suspend fun clearChecked(houseId: String) {
        groceryItemDao.deleteCheckedForHouse(houseId)
    }
}
