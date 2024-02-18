package com.example.hisaabkitaab.repository

import com.example.hisaabkitaab.db.dao.FriendsDao
import com.example.hisaabkitaab.db.entity.Friend
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FriendsRepository @Inject constructor(
    private val friendsDao: FriendsDao
) : BaseRepo() {

    val allFriends : Flow<List<Friend>> = friendsDao.getAllFriends()

    suspend fun insertFriend(friend: Friend) {
        roomSafeCall { friendsDao.insertFriend(friend) }
    }

    suspend fun deleteFriend(friend: Friend) {
        roomSafeCall { friendsDao.deleteFriend(friend) }
    }

}