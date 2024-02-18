package com.example.hisaabkitaab.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hisaabkitaab.db.converters.ArrayListOfStringToString
import com.example.hisaabkitaab.db.converters.DateConverter
import java.util.Date

@Entity(tableName = "user_table")
@TypeConverters(ArrayListOfStringToString::class, DateConverter::class)
data class User (
    @PrimaryKey var email: String = "",
    var name : String? = "",
    var phone : String? = "",
    var verified : Boolean? = false,
    var currentExpenseIds : ArrayList<String> = arrayListOf(),
    var uploadAllExpenseAfterCountReach : Int = 5, // Default will be 5
    var lastUpdateDate : Date? = Date(System.currentTimeMillis()), // last date when user upload expenses to firestore
)