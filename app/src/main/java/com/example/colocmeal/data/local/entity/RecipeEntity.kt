package com.example.colocmeal.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recipes",
    indices = [Index("houseId"), Index("authorId")]
)
data class RecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String? = null,
    val ingredients: List<String>,
    val authorId: String,
    val authorName: String,
    val houseId: String? = null,
    val isShared: Boolean = false
)
