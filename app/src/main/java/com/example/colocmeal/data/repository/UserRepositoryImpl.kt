package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.UserDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toDto
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.data.remote.UserFirestoreDataSource
import com.example.colocmeal.di.FirebaseProvider
import com.example.colocmeal.domain.model.User
import com.example.colocmeal.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val remote: UserFirestoreDataSource,
    private val scope: CoroutineScope
) : UserRepository {

    fun startSync(uid: String) {
        scope.launch {
            remote.observeUser(uid).collect { dto ->
                if (dto != null) userDao.upsert(dto.toDomain().toEntity())
            }
        }
    }

    override fun observeUser(uid: String): Flow<User?> =
        userDao.getUserById(uid).map { it?.toDomain() }

    override suspend fun upsertUser(user: User) {
        remote.upsert(user.toDto())
        userDao.upsert(user.toEntity())
    }

    override suspend fun setUserHouse(uid: String, houseId: String?) {
        remote.setHouseId(uid, houseId)
        userDao.updateHouseId(uid, houseId)
    }
}