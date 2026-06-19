package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun startSync(uid: String)

    fun observeUser(uid: String): Flow<User?>

    suspend fun upsertUser(user: User)

    suspend fun setUserHouse(uid: String, houseId: String?)
}
