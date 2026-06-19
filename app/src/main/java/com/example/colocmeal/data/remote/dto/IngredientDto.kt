package com.example.colocmeal.data.remote.dto

import com.google.firebase.firestore.Exclude

data class IngredientDto(
    @get: Exclude val id: String = "",
    val houseId: String = "",
    val name: String = "",
    val nameNormalized: String = "",
    val aisle: String = "OTHER"
)