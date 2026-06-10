package com.example.colocmeal.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.colocmeal.data.local.AppDatabase
import com.example.colocmeal.data.local.entity.HouseEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HouseDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: HouseDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.houseDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    private fun house() = HouseEntity(
        id = "h1",
        name = "Casa",
        inviteCode = "ABC123",
        creatorId = "u1",
        memberIds = listOf("u1")
    )

    @Test
    fun upsertAndObserveHouse() = runTest {
        dao.upsert(house())

        assertEquals(house(), dao.getHouseById("h1").first())
    }

    @Test
    fun getHouseByInviteCode() = runTest {
        dao.upsert(house())

        assertEquals(house(), dao.getHouseByInviteCode("ABC123"))
        assertNull(dao.getHouseByInviteCode("UNKNOWN"))
    }

    @Test
    fun upsertReplacesMemberIds() = runTest {
        dao.upsert(house())

        val updated = house().copy(memberIds = listOf("u1", "u2"))
        dao.upsert(updated)

        assertEquals(updated, dao.getHouseById("h1").first())
    }

    @Test
    fun deleteRemovesHouse() = runTest {
        dao.upsert(house())

        dao.delete(house())

        assertNull(dao.getHouseById("h1").first())
    }
}
