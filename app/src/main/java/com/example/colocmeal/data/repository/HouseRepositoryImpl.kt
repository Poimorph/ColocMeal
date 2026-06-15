package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.HouseDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toDto
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.data.remote.HouseFirestoreDataSource
import com.example.colocmeal.di.FirebaseProvider
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.repository.HouseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// TODO 1.2 : complete remote
class HouseRepositoryImpl(
    private val houseDao: HouseDao,
    private val remote : HouseFirestoreDataSource,
    private val scope: CoroutineScope
) : HouseRepository {

    fun startSync(houseId: String){
        scope.launch {
            remote.observeHouse(houseId).collect{
                dto ->
                if(dto != null) houseDao.upsert(dto.toDomain().toEntity())
            }
        }
    }


    override fun observeHouse(id: String): Flow<House?> =
        houseDao.getHouseById(id).map { it?.toDomain() }

    override suspend fun getHouseByInviteCode(inviteCode: String): House? =
        remote.getByInviteCode(inviteCode)?.toDomain()

    override suspend fun upsertHouse(house: House) {
        remote.upsert(house.toDto())
        houseDao.upsert(house.toEntity())
    }

    override suspend fun addMember(houseId: String, userId: String) {
        remote.addMember(houseId, userId)
    }
}
