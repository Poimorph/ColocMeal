package com.example.colocmeal.data.remote.dto

data class GroceryItemDto(
    val id: String = "",
    val houseId: String = "",
    val name: String = "",
    val nameNormalized: String = "",
    val aisle: String = "",
    val isChecked: Boolean = false,
    val source: String = "AUTO",
    val addedBy: String = ""
)