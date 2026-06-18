package com.example.colocmeal.di

import java.time.LocalDate


class HouseSession(
    private val container: AppContainer
) {
    private var started: String? = null

    fun start(houseId: String) {
        if (started == houseId) return
        started = houseId
        val uid = container.authRepository.currentUid

        container.houseRepository.startSync(houseId)
        container.recipeRepository.startSync(houseId, uid)
        container.mealPlanRepository.startSync(houseId, LocalDate.now())
        container.groceryRepository.startSync(houseId)
    }
}