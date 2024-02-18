package com.example.hisaabkitaab.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hisaabkitaab.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("Select * from user_table")
    fun getUser(): Flow<User>

    @Query("Delete from user_table")
    suspend fun clear()
}