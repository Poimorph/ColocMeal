package com.example.colocmeal.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUid : String?

    fun authState() : Flow<String?> //emits uid or null on sign-in and signout

    suspend fun signUp(email: String, password: String, displayName: String): Result<String> // uid
    suspend fun signIn(email: String, password: String): Result<String>
    fun signOut()

}