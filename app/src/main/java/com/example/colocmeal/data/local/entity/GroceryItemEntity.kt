package com.example.colocmeal.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.Source

@Entity(
    tableName = "grocery_items",
    indices = [Index("houseId")]
)
data class GroceryItemEntity(
    @PrimaryKey val id: String,
    val houseId: String,
    val name: String,
    val nameNormalized: String,
    val aisle: Aisle,
    val isChecked: Boolean = false,
    val source: Source,
    val addedBy: String
)
