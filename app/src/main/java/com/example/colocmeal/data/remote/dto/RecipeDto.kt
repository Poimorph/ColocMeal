package com.example.colocmeal.data.remote.dto

import com.google.firebase.firestore.Exclude

data class RecipeDto(
    @get: Exclude val id: String = "",
    val name: String = "",
    val description: String? = "",
    val ingredients: List<String> = emptyList(),
    val authorId: String = "",
    val authorName: String = "",
    val houseId: String = "",     // null if private
    val isShared: String = "false"    // set at creation, never changes
)
