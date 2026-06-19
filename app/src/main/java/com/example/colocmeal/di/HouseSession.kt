package com.example.colocmeal.di

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters


class HouseSession(
    private val container: AppContainer
) {
    private var started: String? = null

    fun start(houseId: String) {
        if (started == houseId) return
        started = houseId
        val uid = container.authRepository.currentUid

        val weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        container.houseRepository.startSync(houseId)
        container.recipeRepository.startSync(houseId, uid)
        container.mealPlanRepository.startSync(houseId, weekStart)
        container.groceryRepository.startSync(houseId)
        container.ingredientRepository.startSync(houseId)
    }
}
