package com.example.colocmeal.data.remote.dto

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class GroceryItemDto(
    @get: Exclude val id: String = "",
    val houseId: String = "",
    val name: String = "",
    val nameNormalized: String = "",
    val aisle: String = "",

    @get:PropertyName("isChecked")
    @set:PropertyName("isChecked")
    var isChecked: Boolean = false,

    val checkedByName: String = "",
    val source: String = "AUTO",
    val addedBy: String = ""
)
