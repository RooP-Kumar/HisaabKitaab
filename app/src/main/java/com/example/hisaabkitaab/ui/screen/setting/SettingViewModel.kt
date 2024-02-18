package com.example.hisaabkitaab.ui.screen.setting

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.hisaabkitaab.api.resource.Resource
import com.example.hisaabkitaab.db.database.TransactionDatabase
import com.example.hisaabkitaab.getAppState
import com.example.hisaabkitaab.repository.AuthRepository
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.ui.utility.Constants
import com.example.hisaabkitaab.ui.utility.LoadingState
import com.example.hisaabkitaab.ui.utility.SyncingState
import com.example.hisaabkitaab.ui.utility.io
import com.example.hisaabkitaab.workmanager.MyExpenseUploaderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Duration
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val application : Application,
    private val db : TransactionDatabase,
    private val myExpenseRepository: MyExpenseRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // Companion Object
    companion object {
        private const val TAG = "SettingViewModel"
    }

    // initializing Ui States

    val settingUiState by lazy { SettingUiState() }

    // initializing Dao
    private val myExpenseDao by lazy { db.getMyExpenseDao() }

    // Initializing Work Manager
    private val workManager = WorkManager.getInstance(application)



    fun syncData() {
        myExpenseRepository.uploadExpensesSingleTime(application)
    }

    fun logout() {

        settingUiState.apply {

            io {
                db.getUserDao().clear()
                db.getMyExpenseDao().clear()
                when(authRepository.logout())
                {
                    is Resource.SUCCESS -> {
                        delay(1300)
                        loadingState.value = LoadingState.SUCCESS
                    }

                    is Resource.FAILURE -> {
                        loadingState.value = LoadingState.FAILURE
                    }
                }
            }

        }

    }
}