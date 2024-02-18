package com.example.hisaabkitaab.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.hisaabkitaab.api.MyExpenseApi
import com.example.hisaabkitaab.db.database.TransactionDatabase
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.workmanager.MyExpenseUploaderWorker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val dao = TransactionDatabase.getInstance(this).getMyExpenseDao()
        val configuration = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(
                UploadExpenseWorkerFactory(
                    MyExpenseRepository(
                        context = this,
                        dao,
                        MyExpenseApi()
                    )
                )
            )
            .build()

        WorkManager.initialize(this, configuration)
    }

}

class UploadExpenseWorkerFactory (private val addExpenseRepo : MyExpenseRepository) :
    WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return MyExpenseUploaderWorker(appContext, workerParameters, addExpenseRepo)
    }

}