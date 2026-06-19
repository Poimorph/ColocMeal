package com.example.colocmeal.domain.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InviteCodeTest {

    private val allowed = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toSet()

    @Test
    fun `generate has the requested length`() {
        assertEquals(6, InviteCode.generate().length)
        assertEquals(8, InviteCode.generate(8).length)
    }

    @Test
    fun `generate uses only allowed characters`() {
        repeat(500) {
            InviteCode.generate(10).forEach { c ->
                assertTrue("Unexpected char '$c'", c in allowed)
            }
        }
    }

    @Test
    fun `generate excludes ambiguous characters I O 0 1`() {
        val banned = setOf('I', 'O', '0', '1')
        repeat(500) {
            InviteCode.generate(12).forEach { c ->
                assertFalse("Ambiguous char '$c' should be excluded", c in banned)
            }
        }
    }
}