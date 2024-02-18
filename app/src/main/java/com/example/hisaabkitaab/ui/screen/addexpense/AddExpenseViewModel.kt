package com.example.hisaabkitaab.ui.screen.addexpense

import android.app.Application
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.ui.screen.dialog.AddExpenseAddItemDialogUiState
import com.example.hisaabkitaab.ui.utility.FragmentDialog
import com.example.hisaabkitaab.ui.utility.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(

    private val application: Application,
    private val addMyExpenseRepository: MyExpenseRepository

) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    private val _allMyExpense : MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
    val allMyExpense : LiveData<ArrayList<String>> get() = _allMyExpense

    val addExpenseAddItemDialogUiState by lazy { AddExpenseAddItemDialogUiState() }

    fun addMyExpenseToList(expense: String) {
        val temp = _allMyExpense.value
        temp?.add(expense)
        _allMyExpense.postValue(temp!!)
    }

    fun clearList() {
        _allMyExpense.postValue(arrayListOf())
    }

    fun insertExpense(expense: MyExpense) {
        launch {
            addMyExpenseRepository.insertExpense(expense)
            }
    }

    fun deleteExpense(expense: MyExpense){
        launch {
            addMyExpenseRepository.deleteExpense(expense)
        }
    }
}