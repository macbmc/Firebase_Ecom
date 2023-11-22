package com.example.firebaseecom.local

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converter {

    @TypeConverter
    fun fromMapType(currency: String): Map<String, String>? {
        val type = Types.newParameterizedType(
            MutableMap::class.java,
            String::class.java,
            String::class.java
        )
        return Moshi.Builder().build().adapter<Map<String, String>>(type).fromJson(currency)
    }

    @TypeConverter
    fun fromString(map: Map<String, String>): String {
        val type = Types.newParameterizedType(
            MutableMap::class.java,
            String::class.java,
            String::class.java
        )
        return Moshi.Builder().build().adapter<Map<String, String>>(type).toJson(map)
    }
}