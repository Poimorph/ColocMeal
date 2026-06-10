package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.UserDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.domain.model.User
import com.example.colocmeal.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override fun observeUser(uid: String): Flow<User?> =
        userDao.getUserById(uid).map { it?.toDomain() }

    override suspend fun upsertUser(user: User) {
        userDao.upsert(user.toEntity())
    }

    override suspend fun setUserHouse(uid: String, houseId: String?) {
        userDao.updateHouseId(uid, houseId)
    }
}
