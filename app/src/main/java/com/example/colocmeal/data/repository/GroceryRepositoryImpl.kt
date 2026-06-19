package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.GroceryItemDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toDto
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.data.remote.GroceryFirestoreDataSource
import com.example.colocmeal.di.FirebaseProvider
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.repository.GroceryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.UUID

class GroceryRepositoryImpl(
    private val groceryItemDao: GroceryItemDao,
    private val remote: GroceryFirestoreDataSource,
    private val scope: CoroutineScope
) : GroceryRepository {

    override fun startSync(houseId:String){
        scope.launch {
            remote.observeItems(houseId).collect{
                    dtos->
                groceryItemDao.upsertAll( dtos.map{it.toDomain().toEntity() })
            }
        }
    }

    override fun observeItems(houseId: String): Flow<List<GroceryItem>> =
        groceryItemDao.getItemsForHouse(houseId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun addOrMergeItem(item: GroceryItem) {
        val existing = groceryItemDao.findByNormalizedName(item.houseId, item.nameNormalized)
        if (existing != null) return
        val toInsert = if (item.id.isBlank()) item.copy(id = UUID.randomUUID().toString()) else item
        groceryItemDao.upsert(toInsert.toEntity())
        remote.upsert(toInsert.toDto())
    }
    override suspend fun setChecked(item: GroceryItem, checked: Boolean, checkedByName: String) {
        val who = if (checked) checkedByName else ""
        groceryItemDao.setChecked(item.id, checked, who)
        remote.setChecked(item.id, checked, who)
    }

    override suspend fun deleteItem(item: GroceryItem) {
        groceryItemDao.delete(item.toEntity())
        remote.delete(item.id)
    }

    override suspend fun clearChecked(houseId: String) {
        groceryItemDao.deleteCheckedForHouse(houseId)
        remote.deleteChecked(houseId)
    }
}
