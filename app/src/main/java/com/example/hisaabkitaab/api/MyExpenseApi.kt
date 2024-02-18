package com.example.hisaabkitaab.api

import android.util.Log
import com.example.hisaabkitaab.api.resource.Response
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.getAppState
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MyExpenseApi @Inject constructor() {
    companion object {
        const val TAG = "my expense api"
    }

    suspend fun getMyExpense() : Response<List<MyExpense>> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        val response = Response(value = listOf<MyExpense>())
        Log.d(TAG, "getMyExpense: ${getAppState().user}")
        getAppState().user?.let {user ->
            db.collection("User").document(user.email).collection("myExpense")
                .get()
                .addOnSuccessListener {
                    Log.d(TAG, "getMyExpense: ${it.toObjects(MyExpense::class.java)}")
                    response.value = it.toObjects(MyExpense::class.java)
                    response.status = true
                    response.message = "Success!"
                    continuation.resume(response)
                }
                .addOnFailureListener {
                    response.status = false
                    response.message = it.toString()
                    continuation.resume(response)
                }
        }
    }

    suspend fun uploadMyExpense(expense: MyExpense) : Response<Unit> = suspendCoroutine {continuation ->
        var docRef : DocumentReference? = null
        getAppState().user?.let {localUser ->
            docRef = FirebaseFirestore.getInstance().collection("User").document(localUser.email).collection("myExpense").document(expense.id)
        }
        val response = Response(value = Unit)
        docRef?.let {
            it
                .set(expense)
                .addOnSuccessListener {
                    response.status = true
                    response.message = "Success!"
                    continuation.resume(response)
                }
                .addOnFailureListener {
                    response.status = false
                    response.message = it.message.toString()
                    continuation.resume(response)
                }
        }
    }

    suspend fun uploadMyExpense(expenses: List<MyExpense>) : Response<ArrayList<MyExpense>> = suspendCoroutine {continuation ->
        val db = FirebaseFirestore.getInstance()
        val response = Response(value = ArrayList<MyExpense>())
        db.runTransaction {
            expenses.forEach {expense ->
                val tempExpense = expense.copy(synced = true)
                getAppState().user?.let {user ->
                    val docRef = db.collection("User").document(user.email).collection("myExpense").document(tempExpense.id)
                    docRef.set(tempExpense)
                    response.value.add(tempExpense)
                }
            }
        }
            .addOnSuccessListener {
                response.status = true
                response.message = "Success!"
                continuation.resume(response)
            }
            .addOnFailureListener {
                response.status = false
                response.message = it.message.toString()
                continuation.resume(response)
            }
    }
}