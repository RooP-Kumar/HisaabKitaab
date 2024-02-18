package com.example.hisaabkitaab.ui.screen.myepense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.ui.utility.ScreenLoadingState
import com.example.hisaabkitaab.ui.utility.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyExpenseViewModel @Inject constructor(
    private val myExpenseRepository: MyExpenseRepository
): ViewModel() {
    val currentMonthExpenses = myExpenseRepository.getExpenseLastMonth()
    val myExpenseUiState by lazy { MyExpenseUiState() }

    fun insertExpense(expense: MyExpense) {
        launch {
            myExpenseRepository.insertExpense(expense)
        }
    }

    fun deleteExpense(expense: MyExpense) {
        launch {
            myExpenseRepository.deleteExpense(expense)
        }
    }
}