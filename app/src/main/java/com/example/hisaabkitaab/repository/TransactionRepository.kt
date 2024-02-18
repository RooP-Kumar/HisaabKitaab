package com.example.hisaabkitaab.repository

import com.example.hisaabkitaab.db.dao.TransactionDao
import com.example.hisaabkitaab.db.entity.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    val allTransaction : Flow<List<Transaction>> = transactionDao.getAllTransaction()

    suspend fun addTransaction(trans : Transaction) {
        transactionDao.addTransaction(trans)
    }

    suspend fun deleteTransaction(trans : Transaction) {
        transactionDao.deleteTransaction(trans)
    }

    fun getTransaction(id : String) : Flow<Transaction> {
        return transactionDao.getTransaction(id)
    }

}