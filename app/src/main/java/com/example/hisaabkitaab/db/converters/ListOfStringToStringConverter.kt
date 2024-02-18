package com.example.hisaabkitaab.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListOfStringToStringConverter {
    @TypeConverter
    fun toString(lvalue : List<String>) : String {
        val gson = Gson()
        return gson.toJson(lvalue)
    }

    @TypeConverter
    fun fromString(value: String) : List<String> {
        val gson = Gson()
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }
}