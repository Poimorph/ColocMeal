package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.MealPlanDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.repository.MealPlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class MealPlanRepositoryImpl(
    private val mealPlanDao: MealPlanDao
) : MealPlanRepository {

    override fun observeWeek(houseId: String, weekStart: LocalDate): Flow<List<MealPlan>> =
        mealPlanDao.getMealPlansForWeek(houseId, weekStart).map { entities -> entities.map { it.toDomain() } }

    override suspend fun upsertMealPlan(mealPlan: MealPlan) {
        mealPlanDao.upsert(mealPlan.toEntity())
    }

    override suspend fun deleteMealPlan(mealPlan: MealPlan) {
        mealPlanDao.delete(mealPlan.toEntity())
    }
}
