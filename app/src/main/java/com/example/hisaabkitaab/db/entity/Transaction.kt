package com.example.hisaabkitaab.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hisaabkitaab.db.converters.CustomTransactionTypeConverter

@Entity(tableName = "transaction_table")
@TypeConverters(CustomTransactionTypeConverter::class)
data class Transaction (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    var title : String = "",
    var amount : Long = 0L,
    var friends : List<Friend> = listOf(),
    var fullyPaid : Boolean = false,
    var paidBy : String = "",
    var excludeMe : Boolean? = false,
    var howManyPaid : Int? = 0
)