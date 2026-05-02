package com.example.colocmeal.domain.model

data class User(
    val uid: String,
    val displayName: String,
    val email: String,
    val houseId: String? = null
)
