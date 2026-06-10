package com.example.colocmeal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "houses")
data class HouseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val inviteCode: String,
    val creatorId: String,
    val memberIds: List<String>
)
