package com.example.hisaabkitaab.repository

import com.example.hisaabkitaab.api.AuthApi
import com.example.hisaabkitaab.api.resource.Resource
import com.example.hisaabkitaab.api.resource.Response
import com.example.hisaabkitaab.db.dao.MyExpenseDao
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.db.entity.User
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val myExpenseDao: MyExpenseDao
) : BaseRepo() {
    suspend fun signUp(user: User, pass : String) : Resource<Response<Boolean>> {
        return safeApi {
            api.signUp(user, pass)
        }
    }

    suspend fun signIn(email: String, pass: String) : Resource<Response<Unit>> {
        return safeApi {
            api.signIn(email, pass)
        }
    }

    suspend fun storeMyExpenseFromFirebase(myExpenses: List<MyExpense>) : Resource<Unit> {
        return roomSafeCall {
            try {
                myExpenseDao.insertExpense(myExpenses)
                Resource.SUCCESS(Unit)
            } catch (e: Exception) {
                Resource.FAILURE(e.toString())
            }

        }
    }

    suspend fun logout() : Resource<Response<Unit>> {
        return safeApi {
            api.logout()
        }
    }
}