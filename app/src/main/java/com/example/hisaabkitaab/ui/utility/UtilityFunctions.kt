package com.example.hisaabkitaab.ui.utility

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.ui.screen.addexpense.AddExpenseViewModel
import com.example.hisaabkitaab.ui.screen.dialog.AddExpenseAddItemDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


fun launch(call : suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        call()
    }
}

fun main(call: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        call()
    }
}

fun io(call: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        call()
    }
}

suspend fun <T> async(call: suspend () -> T) : Deferred<T>{
    return CoroutineScope(Dispatchers.IO).async {
        call()
    }
}

fun getRandomId() : String {
    return "randomId" + System.currentTimeMillis()
}


// Type Converter
fun <T, U> convert(from : T) : U {
    val gson = Gson()
    return if(object : TypeToken<T>() {}.type != object : TypeToken<String>() {}.type){
        val jsonString = gson.toJson(from)
        gson.fromJson(jsonString, object : TypeToken<U>() {}.type)
    } else {
        gson.fromJson(from.toString(), object : TypeToken<U>() {}.type)
    }
}

// Dialog
@AndroidEntryPoint
class FragmentDialog(
    private val dismissOnOutsideClick : Boolean = true
): DialogFragment() {

    private val viewModel : AddExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireDialog().window?.setBackgroundDrawableResource(android.R.color.transparent)
        requireDialog().setCanceledOnTouchOutside(dismissOnOutsideClick)
        return ComposeView(requireContext()).apply {
            setContent {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.generalPadding)))
                        .background(colorResource(id = R.color.surface))
                        .padding(dimensionResource(id = R.dimen.generalPadding))
                ) {
                    AddExpenseAddItemDialog(viewModel = viewModel) { type, title, amount ->
                        viewModel.addMyExpenseToList("$type$MY_EXPENSE_TITLE_AMOUNT_SEPARATOR$title$MY_EXPENSE_TITLE_AMOUNT_SEPARATOR$amount")
                        requireDialog().hide()
                    }
                }
            }
        }
    }

}


@Composable
fun CustomProgressLayout(
    status : MutableState<LoadingState>,
    successButEmpty : MutableState<Boolean>? = null,
    emptyContent : @Composable () -> Unit = {},
    mainContent : @Composable () -> Unit ={}
) {
    val showProgressBar = remember { mutableStateOf(false) }
    val showEmptyScreen = remember { mutableStateOf(false) }
    val showScreen = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = status.value, key2 = successButEmpty?.value) {
        when(status.value) {
            LoadingState.IDLE -> {
                showProgressBar.value = false
                showEmptyScreen.value = false
                showScreen.value = true
            }
            LoadingState.LOADING -> {
                showProgressBar.value = true
                showEmptyScreen.value = false
                showScreen.value = false
            }
            LoadingState.SUCCESS -> {
                showProgressBar.value = false
                showEmptyScreen.value = successButEmpty?.value ?: false
                showScreen.value = !successButEmpty?.value!! ?: true
            }
            LoadingState.FAILURE -> {
                showProgressBar.value = false
                showEmptyScreen.value = true
                showScreen.value = false
            }
        }
    }

    AnimatedContent(targetState = showProgressBar, label = "show progress") {
        if(it.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }

    AnimatedContent(targetState = showEmptyScreen, label = "show empty") {
        if(it.value) {
            emptyContent()
        }
    }

    AnimatedContent(targetState = showScreen, label = "show screen") {
        if(it.value) {
            mainContent()
        }
    }
}
