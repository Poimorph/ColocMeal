package com.example.colocmeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.colocmeal.data.local.entity.HouseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseDao {

    @Query("SELECT * FROM houses WHERE id = :id")
    fun getHouseById(id: String): Flow<HouseEntity?>

    @Query("SELECT * FROM houses WHERE inviteCode = :inviteCode")
    suspend fun getHouseByInviteCode(inviteCode: String): HouseEntity?

    @Upsert
    suspend fun upsert(house: HouseEntity)

    @Delete
    suspend fun delete(house: HouseEntity)
}
