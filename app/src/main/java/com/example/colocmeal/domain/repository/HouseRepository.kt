package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.House
import kotlinx.coroutines.flow.Flow

interface HouseRepository {

    fun observeHouse(id: String): Flow<House?>

    suspend fun generateUniqueInviteCode(maxAttempts: Int): String
    suspend fun getHouseByInviteCode(inviteCode: String): House?

    suspend fun upsertHouse(house: House)

    suspend fun addMember(houseId: String, userId: String)
    fun startSync(houseId: String)
}
