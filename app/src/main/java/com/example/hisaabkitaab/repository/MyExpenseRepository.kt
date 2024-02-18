package com.example.hisaabkitaab.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.hisaabkitaab.api.MyExpenseApi
import com.example.hisaabkitaab.api.resource.Resource
import com.example.hisaabkitaab.api.resource.Response
import com.example.hisaabkitaab.db.dao.MyExpenseDao
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.ui.utility.Constants
import com.example.hisaabkitaab.workmanager.MyExpenseUploaderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class MyExpenseRepository @Inject constructor(

    @ApplicationContext
    val context: Context,
    val myExpenseDao: MyExpenseDao,
    val api : MyExpenseApi

) : BaseRepo() {

    val workManager = WorkManager.getInstance(context)

    val allExpense : Flow<List<MyExpense>> = myExpenseDao.getAllExpense()

    val uploadSingleWorkerInfo : Flow<WorkInfo> = workManager.getWorkInfosByTagLiveData(Constants.SINGLE_TIME_UPLOAD_WORK)
        .asFlow().mapNotNull {

            if(it.isNotEmpty()) it.first() else null

        }

    fun getExpenseLastMonth() : Flow<List<MyExpense>> {
        val todayDate = Date(System.currentTimeMillis())
        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = todayDate

        val firstDayCalendar = Calendar.getInstance()
        firstDayCalendar.set(
            todayCalendar.get(Calendar.YEAR),
            todayCalendar.get(Calendar.MONTH),
            1,
            0,
            0,
            0
        )

        return myExpenseDao.getExpenseLastMonth(firstDayCalendar.time.time, todayDate.time)
    }

    suspend fun insertExpense(expense: MyExpense): Long {
        return roomSafeCall {
            val date = System.currentTimeMillis()
            myExpenseDao.insertExpense(expense.copy(date = Date(date)))
        }
    }

    fun getUnsyncedExpenses() = flow {
        emit(myExpenseDao.getExpensesWhichAreNotSynced())
    }

    suspend fun insertExpense(expenses: List<MyExpense>) {
        return roomSafeCall {
            myExpenseDao.insertExpense(expenses)
        }
    }

    suspend fun deleteExpense(expense: MyExpense) {
        roomSafeCall {
            myExpenseDao.deleteExpense(expense)
        }
    }


    // Firebase Operations

    suspend fun getMyExpenseFromFirebase() : Resource<Response<List<MyExpense>>> {
        return safeApi {
            api.getMyExpense()
        }
    }

    suspend fun uploadExpense(expense: MyExpense) : Resource<Response<Unit>> {
        return safeApi {
            api.uploadMyExpense(expense)
        }
    }

    suspend fun uploadExpense(expenses : List<MyExpense>) : Resource<Response<ArrayList<MyExpense>>> {
        return safeApi {
            api.uploadMyExpense(expenses)
        }
    }

    fun uploadExpensesSingleTime(context: Context) {

        val workRequest = OneTimeWorkRequestBuilder<MyExpenseUploaderWorker>()
            .addTag(Constants.SINGLE_TIME_UPLOAD_WORK)
            .setConstraints(Constraints(NetworkType.CONNECTED))
            .build()

        workManager.beginUniqueWork(Constants.UPLOAD_WORKER_UNIQUE_KEY, ExistingWorkPolicy.REPLACE, workRequest)
            .enqueue()

    }

    fun uploadExpensesPeriodically() {

    }


}