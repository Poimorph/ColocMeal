package com.example.colocmeal.data.remote.dto

import com.google.firebase.firestore.Exclude
import java.time.DayOfWeek
import java.time.LocalDate


data class MealPlanDto(
    @get: Exclude val id: String = "",
    val houseId: String = "",
    val weekStart: String = "",
    val dayOfWeek: String = "",
    val recipeId: String = "",
    val recipeName: String = "",
    val cookId: String = "",
    val cookName : String = ""
)
