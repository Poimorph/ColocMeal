package com.example.colocmeal.ui.auth

import com.example.colocmeal.fakes.FakeAuthRepository
import com.example.colocmeal.util.MainDispatcherRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repo: FakeAuthRepository
    private lateinit var vm: AuthViewModel

    @Before
    fun setUp() {
        repo = FakeAuthRepository()
        vm = AuthViewModel(repo)
    }

    @Test
    fun `invalid email shows validation error and does not call repo`() {
        vm.onEmailChange("not-an-email")
        vm.onPasswordChange("password123")

        vm.submitSignIn()

        assertEquals("Enter a valid email.", vm.uiState.value.error)
        assertEquals(0, repo.signInCalls)
    }

    @Test
    fun `short password shows validation error and does not call repo`() {
        vm.onEmailChange("alice@test.com")
        vm.onPasswordChange("123")

        vm.submitSignIn()

        assertEquals("Password must be at least 6 characters.", vm.uiState.value.error)
        assertEquals(0, repo.signInCalls)
    }

    @Test
    fun `sign up requires a display name`() {
        vm.onEmailChange("alice@test.com")
        vm.onPasswordChange("password123")

        vm.submitSignUp()

        assertEquals("Enter a display name.", vm.uiState.value.error)
        assertEquals(0, repo.signUpCalls)
    }

    @Test
    fun `valid sign in calls repo with trimmed email and clears loading`() {
        repo.signInResult = Result.success("uid-1")
        vm.onEmailChange("  alice@test.com  ")
        vm.onPasswordChange("password123")

        vm.submitSignIn()

        assertEquals(1, repo.signInCalls)
        assertEquals("alice@test.com", repo.lastSignIn?.first)
        assertNull(vm.uiState.value.error)
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun `valid sign up forwards all fields`() {
        vm.onEmailChange("bob@test.com")
        vm.onPasswordChange("password123")
        vm.onDisplayNameChange("Bob")

        vm.submitSignUp()

        assertEquals(1, repo.signUpCalls)
        assertEquals(Triple("bob@test.com", "password123", "Bob"), repo.lastSignUp)
    }

    @Test
    fun `repo failure sets error and clears loading`() {
        repo.signInResult = Result.failure(RuntimeException("Wrong password"))
        vm.onEmailChange("alice@test.com")
        vm.onPasswordChange("password123")

        vm.submitSignIn()

        assertEquals("Wrong password", vm.uiState.value.error)
        assertFalse(vm.uiState.value.isLoading)
    }
}