package com.example.colocmeal.data.mapper

import com.example.colocmeal.data.local.entity.GroceryItemEntity
import com.example.colocmeal.data.local.entity.HouseEntity
import com.example.colocmeal.data.local.entity.MealPlanEntity
import com.example.colocmeal.data.local.entity.RecipeEntity
import com.example.colocmeal.data.local.entity.UserEntity
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.GroceryItem
import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.model.MealPlan
import com.example.colocmeal.domain.model.Recipe
import com.example.colocmeal.domain.model.Source
import com.example.colocmeal.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class MappersTest {

    @Test
    fun `user round trips between domain and entity`() {
        val user = User(uid = "u1", displayName = "Alice", email = "alice@test.com", houseId = "h1")

        assertEquals(user, user.toEntity().toDomain())
        val entity = UserEntity(id = "u2", displayName = "Bob", email = "bob@test.com", houseId = null)
        assertEquals(entity, entity.toDomain().toEntity())
    }

    @Test
    fun `house round trips between domain and entity`() {
        val house = House(
            id = "h1",
            name = "Casa",
            inviteCode = "ABC123",
            creatorId = "u1",
            memberIds = listOf("u1", "u2")
        )

        assertEquals(house, house.toEntity().toDomain())
        val entity = HouseEntity(
            id = "h2",
            name = "Maison",
            inviteCode = "XYZ789",
            creatorId = "u3",
            memberIds = emptyList()
        )
        assertEquals(entity, entity.toDomain().toEntity())
    }

    @Test
    fun `recipe round trips between domain and entity`() {
        val recipe = Recipe(
            id = "r1",
            name = "Pasta",
            description = "Quick dinner",
            ingredients = listOf("Pasta", "Tomato sauce"),
            authorId = "u1",
            authorName = "Alice",
            houseId = "h1",
            isShared = true
        )

        assertEquals(recipe, recipe.toEntity().toDomain())
        val entity = RecipeEntity(
            id = "r2",
            name = "Salad",
            description = null,
            ingredients = emptyList(),
            authorId = "u2",
            authorName = "Bob",
            houseId = null,
            isShared = false
        )
        assertEquals(entity, entity.toDomain().toEntity())
    }

    @Test
    fun `meal plan round trips between domain and entity`() {
        val mealPlan = MealPlan(
            id = "m1",
            houseId = "h1",
            weekStart = LocalDate.of(2026, 6, 8),
            dayOfWeek = 1,
            recipeId = "r1",
            recipeName = "Pasta",
            cookId = "u1",
            cookName = "Alice"
        )

        assertEquals(mealPlan, mealPlan.toEntity().toDomain())
        assertEquals(mealPlan.toEntity(), mealPlan.toEntity().toDomain().toEntity())
    }

    @Test
    fun `grocery item round trips between domain and entity`() {
        val item = GroceryItem(
            id = "g1",
            houseId = "h1",
            name = "Tomatoes",
            nameNormalized = "tomatoes",
            aisle = Aisle.FRUITS_VEGETABLES,
            isChecked = false,
            source = Source.AUTO,
            addedBy = "u1"
        )

        assertEquals(item, item.toEntity().toDomain())
        val entity = GroceryItemEntity(
            id = "g2",
            houseId = "h1",
            name = "Milk",
            nameNormalized = "milk",
            aisle = Aisle.DAIRY,
            isChecked = true,
            source = Source.MANUAL,
            addedBy = "u2"
        )
        assertEquals(entity, entity.toDomain().toEntity())
    }
}
