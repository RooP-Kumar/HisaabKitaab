package com.example.hisaabkitaab.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hisaabkitaab.db.converters.DateConverter
import com.example.hisaabkitaab.db.converters.ListOfStringToStringConverter
import java.util.Date

@Entity(tableName = "my_expense_table")
@TypeConverters(DateConverter::class, ListOfStringToStringConverter::class)
data class MyExpense(
    @PrimaryKey var id : String = "",
    var price : Long? = 0,
    var items : List<String> = listOf(), // Here String will be is this format "type<->title<->price"
    var date : Date? = Date(),
    var synced : Boolean = false
)
