package com.example.hisaabkitaab.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hisaabkitaab.db.entity.Friend
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend: Friend)

    @Delete
    suspend fun deleteFriend(friend: Friend)

    @Query("SELECT * FROM friends_table")
    fun getAllFriends() : Flow<List<Friend>>
}