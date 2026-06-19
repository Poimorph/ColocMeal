package com.example.colocmeal.domain.utils

object InviteCode {
    private const val ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    fun generate(length: Int = 6): String =
        (1..length).map { ALPHABET.random() }.joinToString("")
}