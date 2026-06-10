package com.example.colocmeal.data.local

import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.Source
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `string list round trips through json`() {
        val list = listOf("Pasta", "Tomato sauce", "Cheese")

        val serialized = converters.fromStringList(list)
        assertEquals(list, converters.toStringList(serialized))
    }

    @Test
    fun `empty string list round trips through json`() {
        val list = emptyList<String>()

        val serialized = converters.fromStringList(list)
        assertEquals(list, converters.toStringList(serialized))
    }

    @Test
    fun `local date round trips through iso string`() {
        val date = LocalDate.of(2026, 6, 8)

        val serialized = converters.fromLocalDate(date)
        assertEquals(date, converters.toLocalDate(serialized))
    }

    @Test
    fun `aisle round trips through name for every value`() {
        Aisle.entries.forEach { aisle ->
            assertEquals(aisle, converters.toAisle(converters.fromAisle(aisle)))
        }
    }

    @Test
    fun `source round trips through name for every value`() {
        Source.entries.forEach { source ->
            assertEquals(source, converters.toSource(converters.fromSource(source)))
        }
    }
}
