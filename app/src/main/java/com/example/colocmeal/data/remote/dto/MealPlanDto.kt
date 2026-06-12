package com.example.colocmeal.data.remote.dto

import java.time.DayOfWeek
import java.time.LocalDate


data class MealPlanDto(
    val id: String = "",
    val houseId: String = "",
    val weekStart: String = "",
    val dayOfWeek: String = "",
    val recipeId: String = "",
    val recipeName: String = "",
    val cookId: String = "",
    val cookName : String = ""
)
