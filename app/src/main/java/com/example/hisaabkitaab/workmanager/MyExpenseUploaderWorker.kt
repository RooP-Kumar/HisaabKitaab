package com.example.hisaabkitaab.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.ui.utility.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MyExpenseUploaderWorker(

    context: Context,
    workParams : WorkerParameters,
    private val repository: MyExpenseRepository

) : CoroutineWorker(context, workParams) {

    companion object {

        const val TAG = "upload worker"

    }
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val job = async(Dispatchers.IO) {

            repository.myExpenseDao.getExpensesWhichAreNotSynced()

        }

        val unSyncedData = job.await()

        if(unSyncedData.isNotEmpty()){

            val response = repository.api.uploadMyExpense(unSyncedData)

            if(response.status){

                repository.myExpenseDao.updateExpense(response.value)

                Result.success()

            } else {

                Result.failure()

            }

        } else {

            val outputData = Data.Builder().putString(Constants.UPLOAD_EXPENSE_DATA_TO_FIREBASE_OUTPUT_KEY, "All Databases are synced.").build()

            Result.success(outputData)

        }

    }
}