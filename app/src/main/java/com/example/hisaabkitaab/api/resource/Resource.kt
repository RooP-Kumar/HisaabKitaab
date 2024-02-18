package com.example.hisaabkitaab.api.resource

sealed class Resource<out T> {
    data class SUCCESS<T>(
        var value : T
    ) : Resource<T>()

    data class FAILURE(
        var message : String
    ) : Resource<Nothing>()
}