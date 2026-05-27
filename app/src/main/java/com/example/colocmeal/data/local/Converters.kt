package com.example.colocmeal.data.local

import androidx.room.TypeConverter
import com.example.colocmeal.domain.model.Aisle
import com.example.colocmeal.domain.model.Source
import org.json.JSONArray
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val array = JSONArray(value)
        return List(array.length()) { index -> array.getString(index) }
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = value.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromAisle(value: Aisle): String = value.name

    @TypeConverter
    fun toAisle(value: String): Aisle = Aisle.valueOf(value)

    @TypeConverter
    fun fromSource(value: Source): String = value.name

    @TypeConverter
    fun toSource(value: String): Source = Source.valueOf(value)
}
