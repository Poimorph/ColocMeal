package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.HouseDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.repository.HouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class HouseRepositoryImpl(
    private val houseDao: HouseDao
) : HouseRepository {

    override fun observeHouse(id: String): Flow<House?> =
        houseDao.getHouseById(id).map { it?.toDomain() }

    override suspend fun getHouseByInviteCode(inviteCode: String): House? =
        houseDao.getHouseByInviteCode(inviteCode)?.toDomain()

    override suspend fun upsertHouse(house: House) {
        houseDao.upsert(house.toEntity())
    }

    override suspend fun addMember(houseId: String, userId: String) {
        val house = houseDao.getHouseById(houseId).first() ?: return
        if (userId in house.memberIds) return
        houseDao.upsert(house.copy(memberIds = house.memberIds + userId))
    }
}
