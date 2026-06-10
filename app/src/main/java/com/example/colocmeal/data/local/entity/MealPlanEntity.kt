package com.example.colocmeal.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "meal_plans",
    indices = [Index("houseId", "weekStart")]
)
data class MealPlanEntity(
    @PrimaryKey val id: String,
    val houseId: String,
    val weekStart: LocalDate,
    val dayOfWeek: Int,
    val recipeId: String,
    val recipeName: String,
    val cookId: String,
    val cookName: String
)
