package com.example.hisaabkitaab.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hisaabkitaab.api.resource.Resource
import com.example.hisaabkitaab.db.entity.User
import com.example.hisaabkitaab.repository.AuthRepository
import com.example.hisaabkitaab.repository.MyExpenseRepository
import com.example.hisaabkitaab.ui.utility.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val myExpenseRepository: MyExpenseRepository
) : ViewModel() {

    private val _loading : MutableLiveData<LoadingState> = MutableLiveData(LoadingState.IDLE)
    val loading : LiveData<LoadingState> get() = _loading

    fun signUp(user: User, pass: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _loading.postValue(LoadingState.LOADING)
            when(repository.signUp(user, pass)) {
                is Resource.SUCCESS -> {
                    _loading.postValue(LoadingState.SUCCESS)
                }
                is Resource.FAILURE -> {
                    _loading.postValue(LoadingState.FAILURE)
                }
            }
        }
    }

    fun signIn(email: String, pass: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _loading.postValue(LoadingState.LOADING)
            when(repository.signIn(email, pass)) {
                is Resource.SUCCESS -> {
                    when(val res = myExpenseRepository.getMyExpenseFromFirebase()){
                        is Resource.SUCCESS -> {
                            try {
                                myExpenseRepository.insertExpense(res.value.value)
                                _loading.postValue(LoadingState.SUCCESS)
                            } catch (e: Exception) {
                                _loading.postValue(LoadingState.FAILURE)
                            }
                        }
                        is Resource.FAILURE -> {
                            _loading.postValue(LoadingState.FAILURE)
                        }
                    }
                }
                is Resource.FAILURE -> {
                    _loading.postValue(LoadingState.FAILURE)
                }
            }
        }
    }
}