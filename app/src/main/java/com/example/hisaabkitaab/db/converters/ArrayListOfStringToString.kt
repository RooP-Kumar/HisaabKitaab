package com.example.hisaabkitaab.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ArrayListOfStringToString {
    @TypeConverter
    fun arrayListToString(listValue : ArrayList<String>) : String {
        val gson = Gson()
        return gson.toJson(listValue)

    }

    @TypeConverter
    fun stringToArrayList(str : String): ArrayList<String> {
        val gson = Gson()
        return gson.fromJson(str, object : TypeToken<ArrayList<String>>(){} .type)
    }
}