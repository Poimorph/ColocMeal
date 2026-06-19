package com.example.colocmeal.fakes

import com.example.colocmeal.domain.model.User
import com.example.colocmeal.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserRepository : UserRepository {

    /** Current user observed by observeUser(); set houseId here to simulate "already in a house". */
    val users = MutableStateFlow<User?>(null)

    val syncStarted = mutableListOf<String>()
    val upserted = mutableListOf<User>()
    val setUserHouseCalls = mutableListOf<Pair<String, String?>>()

    override fun startSync(uid: String) {
        syncStarted += uid
    }

    override fun observeUser(uid: String): Flow<User?> = users

    override suspend fun upsertUser(user: User) {
        upserted += user
        users.value = user
    }

    override suspend fun setUserHouse(uid: String, houseId: String?) {
        setUserHouseCalls += uid to houseId
        users.value = users.value?.copy(houseId = houseId)
    }
}