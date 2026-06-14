package com.example.colocmeal.data.remote.dto

import com.google.firebase.firestore.Exclude

data class UserDto(
    @get: Exclude val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val houseId: String = ""

)
