package com.example.colocmeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.colocmeal.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity?>

    @Upsert
    suspend fun upsert(user: UserEntity)

    @Query("UPDATE users SET houseId = :houseId WHERE id = :id")
    suspend fun updateHouseId(id: String, houseId: String?)

    @Delete
    suspend fun delete(user: UserEntity)
}
