package com.example.hisaabkitaab.ui.screen.balance

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.db.entity.Transaction
import com.example.hisaabkitaab.repository.TransactionRepository
import com.example.hisaabkitaab.ui.utility.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {

    val allTransaction : LiveData<List<Transaction>> = transactionRepository.allTransaction
        .asLiveData(Dispatchers.IO)

    fun updateTransaction(transaction: Transaction, friend: Friend) {
        launch {
            if(transaction.howManyPaid == transaction.friends.size) {
                transactionRepository.deleteTransaction(transaction)
            } else {
                val temp = arrayListOf<Friend>()

                transaction.friends.forEach {
                    if (it.phone == friend.phone) {
                        temp.add(friend)
                    } else {
                        temp.add(it)
                    }
                }

                transactionRepository.addTransaction(transaction.copy(friends = temp))

            }
        }
    }

}