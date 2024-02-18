package com.example.hisaabkitaab.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hisaabkitaab.ui.utility.LoadingState
import com.example.hisaabkitaab.ui.utility.ScreenLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor() : ViewModel() {
    val transactionUiState by lazy { TransactionUiState() }
    fun loadScreen() {
        transactionUiState.apply {
            status.value = LoadingState.LOADING
            viewModelScope.launch {
                delay(500L)
                status.value = LoadingState.FAILURE
            }
        }
    }
}