package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observeUser(uid: String): Flow<User?>

    suspend fun upsertUser(user: User)

    suspend fun setUserHouse(uid: String, houseId: String?)
}
