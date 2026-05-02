package com.example.colocmeal.domain.model

data class Recipe(
    val id: String,
    val name: String,
    val description: String? = null,
    val ingredients: List<String>,   // names only, no quantities
    val authorId: String,
    val authorName: String,          // denormalized
    val houseId: String? = null,     // null if private
    val isShared: Boolean = false    // set at creation, never changes
)
