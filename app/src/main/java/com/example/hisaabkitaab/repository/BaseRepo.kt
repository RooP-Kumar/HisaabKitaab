package com.example.hisaabkitaab.repository

import com.example.hisaabkitaab.api.resource.Resource
import com.example.hisaabkitaab.api.resource.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseRepo {
    suspend fun<T> safeApi(
        call : suspend () -> Response<T>
    ) : Resource<Response<T>>{
        return withContext(Dispatchers.IO) {
            val response = call()
            if (response.status) Resource.SUCCESS(value = response)
            else Resource.FAILURE(message = response.message)
        }
    }

    suspend fun<T> roomSafeCall(
        call : suspend () -> T
    ): T {
        return withContext(Dispatchers.IO){
            call()
        }
    }
}