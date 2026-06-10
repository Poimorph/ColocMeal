package com.example.colocmeal.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.colocmeal.data.local.AppDatabase
import com.example.colocmeal.data.local.entity.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: UserDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.userDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun upsertAndObserveUser() = runTest {
        val user = UserEntity(id = "u1", displayName = "Alice", email = "alice@test.com", houseId = null)

        dao.upsert(user)

        assertEquals(user, dao.getUserById("u1").first())
    }

    @Test
    fun observeMissingUserReturnsNull() = runTest {
        assertNull(dao.getUserById("missing").first())
    }

    @Test
    fun updateHouseIdChangesOnlyThatField() = runTest {
        val user = UserEntity(id = "u1", displayName = "Alice", email = "alice@test.com", houseId = null)
        dao.upsert(user)

        dao.updateHouseId("u1", "h1")

        assertEquals(user.copy(houseId = "h1"), dao.getUserById("u1").first())
    }

    @Test
    fun deleteRemovesUser() = runTest {
        val user = UserEntity(id = "u1", displayName = "Alice", email = "alice@test.com", houseId = null)
        dao.upsert(user)

        dao.delete(user)

        assertNull(dao.getUserById("u1").first())
    }
}
