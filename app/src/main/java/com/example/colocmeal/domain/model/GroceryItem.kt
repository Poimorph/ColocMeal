package com.example.colocmeal.domain.model

data class GroceryItem(
    val id: String,
    val houseId: String,
    val name: String,
    val nameNormalized: String,      // toLowerCase().trim().removeAccents()
    val aisle: Aisle,
    val isChecked: Boolean = false,
    val source: Source,
    val addedBy: String
)
