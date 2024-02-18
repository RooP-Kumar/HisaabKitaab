package com.example.hisaabkitaab.ui.utility

const val MY_EXPENSE_TITLE_AMOUNT_SEPARATOR = "<->"

fun String.fetchType() : String? {
    val temp = this.split(MY_EXPENSE_TITLE_AMOUNT_SEPARATOR)
    return if(temp.size == 2){
        null
    } else {
        if (temp[0] == "None") null else temp[0]
    }
}

fun String.fetchTitle() : String {
    val temp = this.split(MY_EXPENSE_TITLE_AMOUNT_SEPARATOR)
    return if(temp.size == 2){
        temp[0]
    } else {
        temp[1]
    }
}

fun String.fetchAmount() : Long {
    val temp = this.split(MY_EXPENSE_TITLE_AMOUNT_SEPARATOR)
    return if(temp.size == 2){
        temp[1].toLong()
    } else {
        temp[2].toLong()
    }
}

