package com.example.hisaabkitaab.api

import android.util.Log
import com.example.hisaabkitaab.api.resource.Response
import com.example.hisaabkitaab.db.dao.UserDao
import com.example.hisaabkitaab.db.entity.User
import com.example.hisaabkitaab.getAppState
import com.example.hisaabkitaab.ui.utility.async
import com.example.hisaabkitaab.ui.utility.io
import com.example.hisaabkitaab.ui.utility.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthApi @Inject constructor(private val userDao: UserDao){
    suspend fun signUp(user: User, pass : String) : Response<Boolean> = suspendCoroutine { continuation ->
        val response = Response(value = false)
        val db = FirebaseFirestore.getInstance().collection("User")
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(user.email, pass)
            .addOnSuccessListener {
                it?.user?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        db.document(user.email).set(user)
                            .addOnSuccessListener {
                                response.status = true
                                continuation.resume(response)
                            }
                            .addOnFailureListener { e->
                                response.message = e.message!!
                                continuation.resume(response)
                            }
                    }
                    ?.addOnFailureListener {
                        response.status = false
                        continuation.resume(response)
                    }
            }
            .addOnFailureListener {
                response.status = false
                continuation.resume(response)
            }
    }

    suspend fun signIn(email: String, pass: String) : Response<Unit> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance().collection("User")
        val auth = FirebaseAuth.getInstance()
        val response = Response(value = Unit)
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                if(it.user?.isEmailVerified!!) {
                    db.document(email).update("verified", true)
                        .addOnSuccessListener {
                            io {
                                val gotUser = async {
                                    getUser(email)
                                }

                                if(gotUser.await()) {
                                    response.status = true
                                    continuation.resume(response)
                                } else {
                                    response.status = false
                                    continuation.resume(response)
                                }
                            }
                        }
                        .addOnFailureListener {e->
                            response.message = e.message!!
                            continuation.resume(response)
                        }
                } else {
                    response.message = "Email is not verified!"
                    continuation.resume(response)
                }

            }
            .addOnFailureListener {
                response.message = it.message!!
                continuation.resume(response)
            }
    }

    suspend fun logout() : Response<Unit> = suspendCoroutine { continuation ->
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
        continuation.resume(Response(Unit, true, "Success"))
    }


    // private functions section
    private suspend fun getUser(email: String) : Boolean = suspendCoroutine {continuation ->
        val db = FirebaseFirestore.getInstance()
        db.collection("User").document(email)
            .get()
            .addOnSuccessListener {
                getAppState().user = it.toObject(User::class.java)!!
                getAppState().user?.lastUpdateDate = Date(System.currentTimeMillis())
                launch { userDao.insertUser(getAppState().user!!) }
                continuation.resume(true)
            }
            .addOnFailureListener {
                continuation.resume(false)
            }
    }
}