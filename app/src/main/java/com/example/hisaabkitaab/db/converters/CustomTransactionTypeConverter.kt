package com.example.hisaabkitaab.db.converters

import androidx.room.TypeConverter
import com.example.hisaabkitaab.db.entity.Friend
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomTransactionTypeConverter {
    @TypeConverter
    fun toString(friends : List<Friend>) : String {
        val gson = Gson()
        return gson.toJson(friends)
    }

    @TypeConverter
    fun fromString(value: String) : List<Friend> {
        val listType = object : TypeToken<List<Friend>>() {}.type
        return Gson().fromJson(value, listType)
    }
}