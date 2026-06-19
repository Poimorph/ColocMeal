package com.example.colocmeal.fakes

import com.example.colocmeal.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRepository(
    override val currentUid: String? = "uid-1",
) : AuthRepository {

    val authStateFlow = MutableStateFlow(currentUid)

    var signInResult: Result<String> = Result.success("uid-1")
    var signUpResult: Result<String> = Result.success("uid-1")

    var signInCalls = 0
    var signUpCalls = 0
    var lastSignIn: Pair<String, String>? = null
    var lastSignUp: Triple<String, String, String>? = null
    var signOutCalls = 0

    override fun authState(): Flow<String?> = authStateFlow

    override suspend fun signIn(email: String, password: String): Result<String> {
        signInCalls++
        lastSignIn = email to password
        return signInResult
    }

    override suspend fun signUp(email: String, password: String, displayName: String): Result<String> {
        signUpCalls++
        lastSignUp = Triple(email, password, displayName)
        return signUpResult
    }

    override fun signOut() {
        signOutCalls++
    }
}