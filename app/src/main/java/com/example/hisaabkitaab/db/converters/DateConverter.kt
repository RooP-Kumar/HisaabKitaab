package com.example.hisaabkitaab.db.converters

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toLong(date: Date?) : Long?{
        return date?.time
    }

    @TypeConverter
    fun toDate(value: Long?) : Date? {
        return value?.let { Date(it) }
    }
}