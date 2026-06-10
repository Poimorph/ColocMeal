package com.example.colocmeal.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.colocmeal.data.local.AppDatabase
import com.example.colocmeal.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: RecipeDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.recipeDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private val sharedRecipe = RecipeEntity(
        id = "r1",
        name = "Pasta",
        description = "Quick dinner",
        ingredients = listOf("Pasta", "Tomato sauce"),
        authorId = "u1",
        authorName = "Alice",
        houseId = "h1",
        isShared = true
    )

    private val privateRecipe = RecipeEntity(
        id = "r2",
        name = "Secret family recipe",
        description = null,
        ingredients = listOf("Mystery"),
        authorId = "u1",
        authorName = "Alice",
        houseId = null,
        isShared = false
    )

    @Test
    fun getRecipesForHouseReturnsOnlySharedRecipes() = runTest {
        dao.upsert(sharedRecipe)
        dao.upsert(privateRecipe)

        val shared = dao.getRecipesForHouse("h1").first()

        assertEquals(listOf(sharedRecipe), shared)
    }

    @Test
    fun getRecipesByAuthorReturnsOnlyPrivateRecipes() = runTest {
        dao.upsert(sharedRecipe)
        dao.upsert(privateRecipe)

        val private = dao.getRecipesByAuthor("u1").first()

        assertEquals(listOf(privateRecipe), private)
    }

    @Test
    fun getRecipeByIdReturnsRecipe() = runTest {
        dao.upsert(sharedRecipe)

        assertEquals(sharedRecipe, dao.getRecipeById("r1"))
        assertNull(dao.getRecipeById("missing"))
    }

    @Test
    fun deleteRemovesRecipe() = runTest {
        dao.upsert(sharedRecipe)

        dao.delete(sharedRecipe)

        assertTrue(dao.getRecipesForHouse("h1").first().isEmpty())
    }
}
