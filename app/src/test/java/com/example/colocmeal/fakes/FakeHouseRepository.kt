package com.example.colocmeal.fakes

import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.repository.HouseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeHouseRepository : HouseRepository {

    var codeToReturn = "ABC234"
    var houseByCode: House? = null

    var generateCalls = 0
    val syncStarted = mutableListOf<String>()
    val upsertedHouses = mutableListOf<House>()
    val addMemberCalls = mutableListOf<Pair<String, String>>()

    override fun startSync(houseId: String) {
        syncStarted += houseId
    }

    override fun observeHouse(id: String): Flow<House?> = flowOf(null)

    override suspend fun generateUniqueInviteCode(maxAttempts: Int): String {
        generateCalls++
        return codeToReturn
    }

    override suspend fun getHouseByInviteCode(inviteCode: String): House? = houseByCode

    override suspend fun upsertHouse(house: House) {
        upsertedHouses += house
    }

    override suspend fun addMember(houseId: String, userId: String) {
        addMemberCalls += houseId to userId
    }
}