package com.example.hisaabkitaab.ui.screen.transaction

import android.view.View
import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.ui.utility.CustomProgressLayout
import com.example.hisaabkitaab.ui.utility.LoadingState
import com.example.hisaabkitaab.ui.utility.ScreenLoadingState

data class TransactionUiState(
    val status: MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE),
    val showProgressBar : MutableState<Boolean> = mutableStateOf(false),
    val showEmptyScreen : MutableState<Boolean> = mutableStateOf(false),
    val showScreen : MutableState<Boolean> = mutableStateOf(false)
)

@Composable
fun Transactions(
    viewModel: TransactionViewModel,
    topAppbarTotalAmountTv: TextView
) {
    topAppbarTotalAmountTv.visibility = View.GONE
    val uiState = viewModel.transactionUiState
    LaunchedEffect(key1 = Unit) {
        viewModel.loadScreen()
    }

    CustomProgressLayout(
        status = uiState.status,
        emptyContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val composition = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty_bucket))
                LottieAnimation(
                    composition = composition.value
                )
            }
        },
        mainContent = { MainUI() }
    )

}

@Composable
private fun MainUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
    ) {
        Text(
            text = "Transactions Screen",
            style = TextStyle(
                colorResource(id = R.color.onBackground)
            )
        )
    }
}