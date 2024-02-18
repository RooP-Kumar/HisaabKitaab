package com.example.hisaabkitaab

import com.example.hisaabkitaab.db.entity.User

fun getAppState(): AppState {
    return AppState.getAppState()
}

class AppState {
    var user: User ? = null
    companion object {
        var instance: AppState? = null
        fun getAppState(): AppState {
            if (instance == null) {
                instance = AppState()
            }
            return instance!!
        }
    }
}