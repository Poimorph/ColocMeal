package com.example.colocmeal.data.remote.dto

import com.google.firebase.firestore.Exclude

data class HouseDto (
    @get: Exclude val id: String = "",
    val name: String = "",
    val inviteCode: String = "",
    val creatorId: String = "",
    val memberIds: List<String> = emptyList()
)