package com.example.colocmeal.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.colocmeal.domain.model.Aisle

@Entity(
    tableName = "ingredients",
    indices = [Index("houseId")]
)
data class IngredientEntity(
    @PrimaryKey val id: String,
    val houseId: String,
    val name: String,
    val nameNormalized: String,
    val aisle: Aisle
)