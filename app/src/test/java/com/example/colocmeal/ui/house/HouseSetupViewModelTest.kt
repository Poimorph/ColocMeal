package com.example.colocmeal.ui.house

import com.example.colocmeal.domain.model.House
import com.example.colocmeal.domain.model.User
import com.example.colocmeal.fakes.FakeAuthRepository
import com.example.colocmeal.fakes.FakeHouseRepository
import com.example.colocmeal.fakes.FakeUserRepository
import com.example.colocmeal.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HouseSetupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var houseRepo: FakeHouseRepository
    private lateinit var userRepo: FakeUserRepository
    private lateinit var authRepo: FakeAuthRepository
    private lateinit var vm: HouseSetupViewModel

    @Before
    fun setUp() {
        houseRepo = FakeHouseRepository()
        userRepo = FakeUserRepository()
        authRepo = FakeAuthRepository(currentUid = "uid-1")
        vm = HouseSetupViewModel(houseRepo, authRepo, userRepo)
    }

    @Test
    fun `blank house name shows error and creates nothing`() {
        vm.onHouseNameChange("   ")

        vm.createHouse()

        assertEquals("Enter a house name.", vm.uiState.value.error)
        assertTrue(houseRepo.upsertedHouses.isEmpty())
        assertTrue(userRepo.setUserHouseCalls.isEmpty())
    }

    @Test
    fun `create generates code, upserts house, and links the user`() {
        houseRepo.codeToReturn = "ABC234"
        vm.onHouseNameChange("Coloc du 5e")

        vm.createHouse()

        assertEquals(1, houseRepo.generateCalls)
        val house = houseRepo.upsertedHouses.single()
        assertEquals("Coloc du 5e", house.name)
        assertEquals("ABC234", house.inviteCode)
        assertEquals("uid-1", house.creatorId)
        assertEquals(listOf("uid-1"), house.memberIds)
        assertEquals("uid-1" to house.id, userRepo.setUserHouseCalls.single())
        assertNull(vm.uiState.value.error)
    }

    @Test
    fun `create is blocked when the user already belongs to a house`() {
        userRepo.users.value = User("uid-1", "Alice", "alice@test.com", houseId = "h0")
        vm.onHouseNameChange("Another house")

        vm.createHouse()

        assertTrue(houseRepo.upsertedHouses.isEmpty())
        assertEquals("You already belong to a house.", vm.uiState.value.error)
    }

    @Test
    fun `join with an unknown code shows invalid code`() {
        houseRepo.houseByCode = null
        vm.onModeChange(SetupMode.JOIN)
        vm.onInviteCodeChange("zzz999")

        vm.joinHouse()

        assertEquals("Invalid code", vm.uiState.value.error)
        assertTrue(houseRepo.addMemberCalls.isEmpty())
    }

    @Test
    fun `join adds the member and links the user`() {
        houseRepo.houseByCode = House("h9", "Casa", "ABC234", "owner", listOf("owner"))
        vm.onModeChange(SetupMode.JOIN)
        vm.onInviteCodeChange("abc234")

        vm.joinHouse()

        assertEquals("h9" to "uid-1", houseRepo.addMemberCalls.single())
        assertEquals("uid-1" to "h9", userRepo.setUserHouseCalls.single())
        assertNull(vm.uiState.value.error)
    }

    @Test
    fun `join is rejected when already a member of that house`() {
        houseRepo.houseByCode = House("h9", "Casa", "ABC234", "owner", listOf("owner", "uid-1"))
        vm.onModeChange(SetupMode.JOIN)
        vm.onInviteCodeChange("abc234")

        vm.joinHouse()

        assertTrue(houseRepo.addMemberCalls.isEmpty())
        assertEquals("You're already a member of this house.", vm.uiState.value.error)
    }

    @Test
    fun `submit dispatches to create in CREATE mode`() {
        vm.onHouseNameChange("Casa")

        vm.submit()

        assertEquals(1, houseRepo.upsertedHouses.size)
    }
}