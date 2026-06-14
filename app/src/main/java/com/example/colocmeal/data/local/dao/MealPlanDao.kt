package com.example.colocmeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.colocmeal.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface MealPlanDao {

    @Query("SELECT * FROM meal_plans WHERE houseId = :houseId AND weekStart = :weekStart")
    fun getMealPlansForWeek(houseId: String, weekStart: LocalDate): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM meal_plans WHERE id = :id")
    suspend fun getMealPlanById(id: String): MealPlanEntity?

    @Upsert
    suspend fun upsert(mealPlan: MealPlanEntity)

    @Upsert
    suspend fun upsertAll(meals: List<MealPlanEntity>)

    @Delete
    suspend fun delete(mealPlan: MealPlanEntity)
}
