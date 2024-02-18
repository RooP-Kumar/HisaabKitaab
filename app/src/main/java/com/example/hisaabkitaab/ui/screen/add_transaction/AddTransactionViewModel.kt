package com.example.hisaabkitaab.ui.screen.add_transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.db.entity.Transaction
import com.example.hisaabkitaab.getAppState
import com.example.hisaabkitaab.repository.FriendsRepository
import com.example.hisaabkitaab.repository.TransactionRepository
import com.example.hisaabkitaab.ui.utility.launch
import com.example.hisaabkitaab.ui.utility.main
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.asJavaRandom

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val transactionRepo: TransactionRepository,
    private val friendsRepository: FriendsRepository
): ViewModel() {

    val allTransaction : Flow<List<Transaction>> = transactionRepo.allTransaction
    val transaction : MutableStateFlow<Transaction> = MutableStateFlow(Transaction())

    val allFriends : Flow<List<Friend>> = friendsRepository.allFriends

    private val _totalAddFriend : MutableLiveData<ArrayList<Friend>> = MutableLiveData(arrayListOf())
    val totalAddFriend : LiveData<ArrayList<Friend>> get() = _totalAddFriend

    private val _paidByFriendList : MutableLiveData<HashMap<String, Friend>> = MutableLiveData(
        getAppState().user?.let {user ->
            hashMapOf(
                user.phone.toString() to Friend(name = user.name, phone = user.phone, paid = true)
            )
        }
    )
    val paidByFriendList : LiveData<HashMap<String, Friend>> get() = _paidByFriendList

    fun addInTotalAddFriend(ind : Int = -1, friend: Friend) {
        val temp = _totalAddFriend.value
        temp?.apply {
            if (ind != -1) add(ind, friend) else add(friend)
            _totalAddFriend.postValue(this)
        }
    }
    fun addInTotalAddFriend(friend: List<Friend>) {
        val temp = _totalAddFriend.value
        temp?.apply {
            clear()
            addAll(friend)
            _totalAddFriend.postValue(this)
        }
    }

    fun deleteFromTotalAddFriend(friend: Friend) {
        val temp = _totalAddFriend.value
        temp?.apply {
            remove(friend)
            _totalAddFriend.postValue(this)
        }
    }

    // PaidByFriendList Operations

    fun addInPaidByFriendList(ind : Int? = null, friend: Friend) {
        val temp = _paidByFriendList.value
        temp?.apply {
            this[friend.phone.toString()] = friend
            _paidByFriendList.postValue(this)
        }
    }
    fun addInPaidByFriendList(friend: List<Friend>) {
        val temp = _paidByFriendList.value
        temp?.apply {
            clear()
            friend.forEach {
                this[it.phone.toString()] = it
            }
            _paidByFriendList.postValue(this)
        }
    }

    fun deleteFromPaidByFriendList(friend: Friend) {
        val temp = _paidByFriendList.value
        temp?.apply {
            remove(friend.phone.toString())
            _paidByFriendList.postValue(this)
        }
    }

    // Room Database Operations
    fun addFriend(friend: Friend) {
        launch {
            friendsRepository.insertFriend(friend.copy(id = getRandomId()))
        }
    }

    fun deleteFriend(friend: Friend) {
        launch {
            friendsRepository.deleteFriend(friend)
        }
    }

    fun addTransaction(trans: Transaction) {
        launch {
            transactionRepo.addTransaction(trans)
            trans.friends.forEachIndexed {ind, it->
                Log.d("asdfasf", "addTransaction: ${it.name}")
                if (ind != 0) deleteFriend(it)
            }
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        launch {
            transactionRepo.deleteTransaction(transaction)
        }
    }

    fun getTransaction(id: String) {
        main {
            transaction.emitAll(transactionRepo.getTransaction(id))
        }
    }

    // Private Functions
    private fun getRandomId() : Int {
        val random = Random.asJavaRandom()
        return random.nextInt()
    }

}