package com.example.colocmeal.data.repository

import com.example.colocmeal.data.local.dao.MealPlanDao
import com.example.colocmeal.data.mapper.toDomain
import com.example.colocmeal.data.mapper.toDto
import com.example.colocmeal.data.mapper.toEntity
import com.example.colocmeal.data.remote.MealFirestoreDataSource
import com.example.colocmeal.di.FirebaseProvider
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.repository.MealPlanRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class MealPlanRepositoryImpl(
    private val mealPlanDao: MealPlanDao,
    private val remote: MealFirestoreDataSource=MealFirestoreDataSource(FirebaseProvider.firestore),
    private val scope: CoroutineScope
) : MealPlanRepository {

    fun startSync(houseId: String, weekStart: LocalDate) {
        scope.launch {
            remote.observeWeek(houseId, weekStart.toString()).collect { dtos ->
                mealPlanDao.upsertAll(dtos.map { it.toDomain().toEntity() })
            }
        }
    }

    override fun observeWeek(houseId: String, weekStart: LocalDate): Flow<List<MealPlan>> =
        mealPlanDao.getMealPlansForWeek(houseId, weekStart).map { entities -> entities.map { it.toDomain() } }

    override suspend fun upsertMealPlan(mealPlan: MealPlan) {
        remote.upsert(mealPlan.toDto())
        mealPlanDao.upsert(mealPlan.toEntity())
    }

    override suspend fun deleteMealPlan(mealPlan: MealPlan) {
        remote.delete(mealPlan.id)
        mealPlanDao.delete(mealPlan.toEntity())
    }
}
