package com.example.colocmeal.domain.model

data class House(
    val id: String,
    val name: String,
    val inviteCode: String,     // 6 chars
    val creatorId: String,
    val memberIds: List<String>
)
