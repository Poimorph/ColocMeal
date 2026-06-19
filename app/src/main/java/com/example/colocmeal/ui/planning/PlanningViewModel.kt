package com.example.colocmeal.ui.planning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.repository.AuthRepository
import com.example.colocmeal.domain.repository.MealPlanRepository
import com.example.colocmeal.domain.repository.RecipeRepository
import com.example.colocmeal.domain.repository.UserRepository
import com.example.colocmeal.ui.container
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.UUID

class PlanningViewModel(
    private val houseId: String,
    private val mealPlanRepository: MealPlanRepository,
    recipeRepository: RecipeRepository,
    authRepository: AuthRepository,
    userRepository: UserRepository,
) : ViewModel() {

    private val uid = authRepository.currentUid.orEmpty()

    private val weekStart: LocalDate =
        LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    private val displayName: StateFlow<String> =
        userRepository.observeUser(uid).map { it?.displayName.orEmpty() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val weeklyPlan: StateFlow<List<MealPlan>> =
        mealPlanRepository.observeWeek(houseId, weekStart)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val recipes: StateFlow<List<Recipe>> =
        recipeRepository.observeSharedRecipes(houseId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addMeal(dayOfWeek: Int, recipe: Recipe) {
        viewModelScope.launch {
            mealPlanRepository.upsertMealPlan(
                MealPlan(
                    id = UUID.randomUUID().toString(),
                    houseId = houseId,
                    weekStart = weekStart,
                    dayOfWeek = dayOfWeek,
                    recipeId = recipe.id,
                    recipeName = recipe.name,
                    cookId = uid,
                    cookName = displayName.value,
                )
            )
        }
    }

    fun deleteMeal(mealId: String) {
        val meal = weeklyPlan.value.find { it.id == mealId } ?: return
        viewModelScope.launch { mealPlanRepository.deleteMealPlan(meal) }
    }

    companion object {
        fun factory(houseId: String) = viewModelFactory {
            initializer {
                PlanningViewModel(
                    houseId,
                    container().mealPlanRepository,
                    container().recipeRepository,
                    container().authRepository,
                    container().userRepository,
                )
            }
        }
    }
}