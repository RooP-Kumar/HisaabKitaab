package com.example.hisaabkitaab.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friends_table")
data class Friend(
    var name : String? = "",
    var phone : String? = "",
    var howMuchPaid : Double = 0.0,
    var paid : Boolean? = false,
    @PrimaryKey(autoGenerate = true) var id : Int? = 0

){
    override fun toString(): String {
        return this.name!!
    }
}
