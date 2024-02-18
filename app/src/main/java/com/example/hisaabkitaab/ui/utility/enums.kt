package com.example.hisaabkitaab.ui.utility

import com.example.hisaabkitaab.R

enum class SyncingState {
    IDLE,
    SCANNING,
    UPLOADING,
    FINISHED,
    ERROR
}

enum class LoadingState {
    IDLE,
    LOADING,
    SUCCESS,
    FAILURE
}

enum class ScreenLoadingState{
    SHOW,
    HIDE,
    EMPTY
}

sealed class BalanceTabs(val title : String, val icon : Int) {
    data object Balance : BalanceTabs("Balance", R.drawable.ic_credit)
    data object Transaction : BalanceTabs("Transaction", R.drawable.ic_cart)
}

fun getBalanceTabs() : List<BalanceTabs> {
    return listOf(BalanceTabs.Balance, BalanceTabs.Transaction)
}