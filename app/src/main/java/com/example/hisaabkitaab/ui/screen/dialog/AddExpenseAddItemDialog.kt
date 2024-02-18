package com.example.hisaabkitaab.ui.screen.dialog

import android.inputmethodservice.Keyboard
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsEndWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.hisaabkitaab.ui.component.GeneralEditText
import com.example.hisaabkitaab.ui.component.GeneralText
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.ui.component.GeneralButton
import com.example.hisaabkitaab.ui.component.GeneralDropDownList
import com.example.hisaabkitaab.ui.component.GeneralDropDownSelector
import com.example.hisaabkitaab.ui.screen.addexpense.AddExpenseViewModel

data class AddExpenseAddItemDialogUiState(
    val type : MutableState<String> = mutableStateOf("None"),
    val title : MutableState<String> = mutableStateOf(""),
    val from : MutableState<String> = mutableStateOf(""),
    val destination : MutableState<String> = mutableStateOf(""),
    val titleError : MutableState<Boolean> = mutableStateOf(false),
    val amount : MutableState<String> = mutableStateOf(""),
    val amountError : MutableState<Boolean> = mutableStateOf(false),
    val dropDownList : SnapshotStateList<String> = mutableStateListOf("Food", "Travel", "Drink", "Goods", "Electronics", "Snacks"),
    val showDropDown : MutableState<Boolean> = mutableStateOf(false),
    val animatedVisibility : MutableState<Boolean> = mutableStateOf(false),

)

@Composable
fun AddExpenseAddItemDialog(
    viewModel: AddExpenseViewModel,
    onAddClick : (String, String, Long) -> Unit
) {

    val uiState = viewModel.addExpenseAddItemDialogUiState

    LaunchedEffect(key1 = uiState.type.value, block = {
        uiState.animatedVisibility.value = uiState.type.value == "Travel"
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GeneralDropDownSelector(
            text = uiState.type,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.halfGeneralPadding)))
                .clickable { uiState.showDropDown.value = uiState.showDropDown.value
                },
            dropDownList = uiState.dropDownList,
            showDropDown = uiState.showDropDown
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.halfGeneralPadding)))

                AnimatedVisibility(visible = uiState.animatedVisibility.value) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        GeneralEditText(
                            text = uiState.from,
                            modifier = Modifier.weight(3f),
                            placeholder = "From",
                            isError = uiState.titleError,
                            keyboardOption = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        GeneralText(
                            text = "To",
                            modifier = Modifier
                                .weight(0.6f),
                            textAlign = TextAlign.Center
                        )

                        GeneralEditText(
                            text = uiState.destination,
                            modifier = Modifier.weight(3f),
                            placeholder = "Destination",
                            isError = uiState.titleError,
                            keyboardOption = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next,
                            )
                        )

                    }
                }

                AnimatedVisibility(visible = !uiState.animatedVisibility.value) {
                    GeneralEditText(
                        text = uiState.title,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = "Title",
                        isError = uiState.titleError,
                        keyboardOption = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.halfGeneralPadding)))

                GeneralEditText(
                    text = uiState.amount,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "Amount",
                    isError = uiState.amountError,
                    keyboardOption = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.halfGeneralPadding)))

                GeneralButton(
                    text = "ADD ITEM",
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if(uiState.title.value.isEmpty() && uiState.type.value != "Travel") {
                        uiState.titleError.value = true
                    } else if (uiState.amount.value.isEmpty() || uiState.amount.value.toLong() <= 0L) {
                        uiState.amountError.value = true
                    } else {
                        val tempTitle = uiState.title.value.ifEmpty { "From ${uiState.from.value} To ${uiState.destination.value}" }
                        onAddClick(uiState.type.value, tempTitle, uiState.amount.value.toLong())
                    }
                }
            }
        }

    }
}