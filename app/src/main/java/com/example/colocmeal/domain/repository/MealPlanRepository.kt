package com.example.colocmeal.domain.repository

import com.example.colocmeal.domain.model.MealPlan
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MealPlanRepository {

    fun observeWeek(houseId: String, weekStart: LocalDate): Flow<List<MealPlan>>

    suspend fun upsertMealPlan(mealPlan: MealPlan)

    suspend fun deleteMealPlan(mealPlan: MealPlan)
}
