package com.example.colocmeal.data.remote.dto

data class RecipeDto(
    val id: String = "",
    val name: String = "",
    val description: String? = "",
    val ingredients: List<String> = emptyList(),
    val authorId: String = "",
    val authorName: String = "",
)