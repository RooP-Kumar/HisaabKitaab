package com.example.hisaabkitaab.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.hisaabkitaab.db.entity.MyExpense
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MyExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: MyExpense) : Long

    @Insert
    suspend fun insertExpense(expense: List<MyExpense>)

    @Update
    suspend fun updateExpense(expenses: List<MyExpense>)

    @Delete
    suspend fun deleteExpense(expense: MyExpense)

    @Query("delete from my_expense_table")
    suspend fun clear()

    @Query("SELECT * FROM my_expense_table ORDER BY date DESC")
    fun getAllExpense(): Flow<List<MyExpense>>

    @Query("SELECT * FROM my_expense_table WHERE id IN (:ids)")
    fun getAllExpenseWithId(ids: List<String>) : List<MyExpense>

    @Query("SELECT * FROM my_expense_table WHERE id = :id")
    fun getMyExpense(id: String) : Flow<MyExpense>

    @Query("SELECT * FROM my_expense_table WHERE date > :from AND date < :to ORDER BY date DESC")
    fun getExpenseLastMonth(from : Long, to : Long): Flow<List<MyExpense>>

    @Query("SELECT * FROM my_expense_table")
    fun getExpenses(): List<MyExpense>

    // for syncing purpose functions
    @Query("SELECT * FROM my_expense_table WHERE synced = 0")
    fun getExpensesWhichAreNotSynced() : List<MyExpense>
}