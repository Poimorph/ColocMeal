package com.example.colocmeal.data.remote.dto

data class HouseDto (
    val id: String = "",
    val name: String = "",
    val inviteCode: String = "",
    val creatorId: String = "",
    val memberIds: List<String> = emptyList()
)