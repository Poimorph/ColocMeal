package com.example.colocmeal.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.colocmeal.data.local.AppDatabase
import com.example.colocmeal.data.local.entity.GroceryItemEntity
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.Source
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GroceryItemDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: GroceryItemDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.groceryItemDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun item(
        id: String,
        houseId: String = "h1",
        name: String = "Tomatoes",
        nameNormalized: String = "tomatoes",
        isChecked: Boolean = false
    ) = GroceryItemEntity(
        id = id,
        houseId = houseId,
        name = name,
        nameNormalized = nameNormalized,
        aisle = Aisle.FRUITS_VEGETABLES,
        isChecked = isChecked,
        source = Source.AUTO,
        addedBy = "u1"
    )

    @Test
    fun getItemsForHouseReturnsItemsForThatHouse() = runTest {
        val itemA = item("g1", houseId = "h1")
        dao.upsert(itemA)
        dao.upsert(item("g2", houseId = "h2"))

        assertEquals(listOf(itemA), dao.getItemsForHouse("h1").first())
    }

    @Test
    fun findByNormalizedNameMatchesWithinHouse() = runTest {
        val itemA = item("g1", houseId = "h1", nameNormalized = "tomatoes")
        dao.upsert(itemA)

        assertEquals(itemA, dao.findByNormalizedName("h1", "tomatoes"))
        assertNull(dao.findByNormalizedName("h2", "tomatoes"))
        assertNull(dao.findByNormalizedName("h1", "milk"))
    }

    @Test
    fun setCheckedTogglesOnlyThatField() = runTest {
        val itemA = item("g1", isChecked = false)
        dao.upsert(itemA)

        dao.setChecked("g1", true)

        assertEquals(itemA.copy(isChecked = true), dao.getItemsForHouse("h1").first().single())
    }

    @Test
    fun deleteCheckedForHouseRemovesOnlyCheckedItems() = runTest {
        val checked = item("g1", isChecked = true)
        val unchecked = item("g2", name = "Milk", nameNormalized = "milk", isChecked = false)
        dao.upsert(checked)
        dao.upsert(unchecked)

        dao.deleteCheckedForHouse("h1")

        assertEquals(listOf(unchecked), dao.getItemsForHouse("h1").first())
    }
}
