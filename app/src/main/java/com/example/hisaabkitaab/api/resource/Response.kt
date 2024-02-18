package com.example.hisaabkitaab.api.resource

data class Response<T> (
    var value: T,
    var status : Boolean = false,
    var message : String = ""
)