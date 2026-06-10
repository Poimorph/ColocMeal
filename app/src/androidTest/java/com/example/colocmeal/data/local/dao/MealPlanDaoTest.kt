package com.example.colocmeal.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.colocmeal.data.local.AppDatabase
import com.example.colocmeal.data.local.entity.MealPlanEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class MealPlanDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: MealPlanDao

    private val weekStart = LocalDate.of(2026, 6, 8)
    private val otherWeekStart = LocalDate.of(2026, 6, 15)

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.mealPlanDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun mealPlan(id: String, houseId: String, weekStart: LocalDate) = MealPlanEntity(
        id = id,
        houseId = houseId,
        weekStart = weekStart,
        dayOfWeek = 1,
        recipeId = "r1",
        recipeName = "Pasta",
        cookId = "u1",
        cookName = "Alice"
    )

    @Test
    fun getMealPlansForWeekFiltersByHouseAndWeek() = runTest {
        val thisWeek = mealPlan("m1", "h1", weekStart)
        dao.upsert(thisWeek)
        dao.upsert(mealPlan("m2", "h1", otherWeekStart))
        dao.upsert(mealPlan("m3", "h2", weekStart))

        val result = dao.getMealPlansForWeek("h1", weekStart).first()

        assertEquals(listOf(thisWeek), result)
    }

    @Test
    fun getMealPlanByIdReturnsPlan() = runTest {
        val plan = mealPlan("m1", "h1", weekStart)
        dao.upsert(plan)

        assertEquals(plan, dao.getMealPlanById("m1"))
        assertNull(dao.getMealPlanById("missing"))
    }

    @Test
    fun deleteRemovesMealPlan() = runTest {
        val plan = mealPlan("m1", "h1", weekStart)
        dao.upsert(plan)

        dao.delete(plan)

        assertTrue(dao.getMealPlansForWeek("h1", weekStart).first().isEmpty())
    }
}
