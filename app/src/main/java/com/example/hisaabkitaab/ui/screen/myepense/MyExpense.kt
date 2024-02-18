package com.example.hisaabkitaab.ui.screen.myepense

import android.util.Log
import android.widget.TextView
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.getAppState
import com.example.hisaabkitaab.ui.component.GeneralText
import com.example.hisaabkitaab.ui.component.GeneralTitleText
import com.example.hisaabkitaab.ui.utility.CustomProgressLayout
import com.example.hisaabkitaab.ui.utility.LoadingState
import com.example.hisaabkitaab.ui.utility.ScreenLoadingState
import com.example.hisaabkitaab.ui.utility.fetchAmount
import com.example.hisaabkitaab.ui.utility.fetchTitle
import com.example.hisaabkitaab.ui.utility.fetchType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

data class MyExpenseUiState(
    val status: MutableState<LoadingState> = mutableStateOf(LoadingState.LOADING),
    val successButEmpty : MutableState<Boolean> = mutableStateOf(false),
    val screenLoadingStatus : MutableState<ScreenLoadingState> = mutableStateOf(ScreenLoadingState.SHOW),
    val topAppbarTotalAmount : MutableState<Double> = mutableDoubleStateOf(0.0)
)

@Composable
fun MyExpense(
    viewModel: MyExpenseViewModel,
    topAppbarTotalAmountTv: TextView
) {
    val uiState = viewModel.myExpenseUiState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val allExpense = viewModel.currentMonthExpenses.collectAsState(initial = listOf())

    if(allExpense.value.isEmpty()){
        uiState.status.value = LoadingState.SUCCESS
        uiState.successButEmpty.value = true
    } else {
        uiState.topAppbarTotalAmount.value = getTotalAmount(allExpense.value)
        uiState.successButEmpty.value = false
        uiState.status.value = LoadingState.SUCCESS
    }

    topAppbarTotalAmountTv.text = stringResource(id = R.string.rupee, uiState.topAppbarTotalAmount.value)

    CustomProgressLayout(
        status = uiState.status,
        successButEmpty = uiState.successButEmpty,
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
        }
    ) {
        MainUi(
            viewModel,
            allExpense.value
        )
    }
}

@Composable
private fun MainUi(
    viewModel: MyExpenseViewModel,
    expenses: List<MyExpense>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background))
            .padding(
                top = dimensionResource(id = R.dimen.halfGeneralPadding),
                bottom = dimensionResource(id = R.dimen.halfGeneralPadding)
            )
    ) {
        items(expenses, key = { it.hashCode() }) { expense ->
            ListItem(expense = expense)
        }
    }
}

@Composable
private fun ListItem(
    expense: MyExpense
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.generalPadding),
                vertical = dimensionResource(id = R.dimen.halfGeneralPadding)
            ),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.halfGeneralPadding)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.my_expense_card_background_color)
        ),
        elevation = CardDefaults.cardElevation(
            dimensionResource(id = R.dimen.normalPadding)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.generalPadding)
                )
                .padding(top = dimensionResource(id = R.dimen.generalPadding))
        ) {
            GeneralText(
                text = "Paid By",
                style = TextStyle().copy(
                    color = colorResource(id = R.color.onSurface)
                )
            )

            GeneralTitleText(
                text = getAppState().user?.name.toString(),
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.End,
                style = TextStyle().copy(
                    color = colorResource(id = R.color.onSurface)
                )
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.halfGeneralPadding),
                    bottom = dimensionResource(id = R.dimen.halfGeneralPadding)
                )
                .height(0.5.dp)
                .background(Color.Black)
        )

        repeat(expense.items.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.generalPadding))
            ) {
                val fetchType = expense.items[it].fetchType()
                val fetchTitle = expense.items[it].fetchTitle()
                val text = if (fetchType == null) {
                    fetchTitle
                } else {
                    "$fetchType : $fetchTitle"
                }
                GeneralText(
                    text = text,
                    modifier = Modifier
                        .weight(1f),
                    style = TextStyle().copy(
                        color = colorResource(id = R.color.onSurface)
                    )
                )

                GeneralText(
                    text = stringResource(id = R.string.rupee, expense.items[it].fetchAmount().toString()),
                    style = TextStyle().copy(
                        color = colorResource(id = R.color.onSurface)
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.halfGeneralPadding),
                    bottom = dimensionResource(id = R.dimen.halfGeneralPadding)
                )
                .height(0.5.dp)
                .background(Color.Black)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.generalPadding)
                )
                .padding(bottom = dimensionResource(id = R.dimen.generalPadding))
        ) {
            GeneralText(
                text = expense.date?.let {
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                }.toString(),
                style = TextStyle().copy(
                    color = colorResource(id = R.color.onSurface)
                )
            )

            GeneralText(
                text = "Total : ",
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.End,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    color = colorResource(id = R.color.onSurface)
                )
            )

            GeneralText(
                text = stringResource(id = R.string.rupee, expense.price.toString()),
                textAlign = TextAlign.End,
                style = TextStyle().copy(
                    color = colorResource(id = R.color.onSurface)
                )
            )

        }
    }
}

private fun getTotalAmount(expenses : List<MyExpense>) : Double {
    var totalPrice : Double = 0.0
    expenses.forEach {
        totalPrice += it.price?.toDouble() ?: 0.0
    }
    return totalPrice
}