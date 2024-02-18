package com.example.hisaabkitaab.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hisaabkitaab.db.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(trans : Transaction)

    @Delete
    suspend fun deleteTransaction(trans : Transaction)

    @Query("select * from transaction_table")
    fun getAllTransaction() : Flow<List<Transaction>>

    @Query("select * from transaction_table where id=:id")
    fun getTransaction(id : String) : Flow<Transaction>
}