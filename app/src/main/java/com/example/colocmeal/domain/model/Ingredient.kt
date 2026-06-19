package com.example.colocmeal.domain.model

data class Ingredient(
    val id: String,
    val houseId: String,
    val name: String,
    val nameNormalized: String,   // lowercase().trim().removeAccents()
    val aisle: Aisle
)
