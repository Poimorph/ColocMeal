package com.example.colocmeal.domain.model

import java.time.LocalDate

data class MealPlan(
    val id: String,
    val houseId: String,
    val weekStart: LocalDate,        // always Monday
    val dayOfWeek: Int,              // 1..7
    val recipeId: String,
    val recipeName: String,          // denormalized
    val cookId: String,
    val cookName: String             // denormalized
)