package com.example.hisaabkitaab.ui.component

import androidx.compose.material3.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.ui.utility.LoadingState

@Composable
fun GeneralDialog(
    loadingState: MutableState<LoadingState>
) {
    val showDialog = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = loadingState.value) {
        when(loadingState.value) {
            LoadingState.LOADING -> {
                showDialog.value = true
            }
            LoadingState.FAILURE -> {
                showDialog.value = false
            }
            LoadingState.SUCCESS -> {
                showDialog.value = false
            }
            LoadingState.IDLE -> {
                showDialog.value = false
            }
        }
    }

    if (showDialog.value) {

        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.dot_animation))

        Dialog(
            onDismissRequest = {},
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.generalPadding),
                        vertical = dimensionResource(id = R.dimen.generalPadding)
                    ),
            ) {

                CircularProgressIndicator(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            top = dimensionResource(id = R.dimen.generalPadding),
                            bottom = dimensionResource(id = R.dimen.generalPadding)
                        )
                )

                Row(
                    modifier = Modifier
                        .padding(
                            bottom = dimensionResource(id = R.dimen.generalPadding),
                            start = dimensionResource(id = R.dimen.generalPadding)
                        )
                        .align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Loading"
                    )

                    LottieAnimation(
                        modifier = Modifier
                            .size(40.dp, 40.dp),
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Inside
                    )

                }
            }
        }
    }
}


@Composable
fun GeneralText(
    text : String,
    modifier : Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style : TextStyle = TextStyle(
        color = colorResource(id = R.color.onBackground),
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.montserrat))
    ),
    maxLines : Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = style,
        maxLines = maxLines
    )
}

@Composable
fun GeneralTitleText(
    text : String,
    modifier : Modifier = Modifier,
    style : TextStyle = TextStyle(
        color = Color.Black,
        fontSize = 16.sp,
        fontFamily = FontFamily(Font(R.font.montserrat))
    ),
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = style,
        maxLines = maxLines
    )
}

@Composable
fun GeneralEditText(
    text : MutableState<String>,
    modifier: Modifier = Modifier,
    placeholder : String? = null,
    isError : MutableState<Boolean> = mutableStateOf(false),
    keyboardOption : KeyboardOptions = KeyboardOptions.Default,
    trailingIcon : @Composable (() -> Unit)? = null,
    enabled : Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(0.7.dp, colorResource(id = R.color.onSurface)),
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.halfGeneralPadding)
                )
            )
            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.halfGeneralPadding)))
            .background(colorResource(id = R.color.background))
            .padding(
                horizontal = dimensionResource(
                    id = R.dimen.generalPadding
                ),
                vertical = dimensionResource(
                    id = R.dimen.generalPadding
                )
            )
    ) {
        val textColor = if(enabled) colorResource(id = R.color.onBackground) else Color.Gray
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            BasicTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                },
                enabled = enabled,
                singleLine = true,
                textStyle = TextStyle.Default.copy(
                    color = textColor
                ),
                keyboardOptions = keyboardOption,
                cursorBrush = Brush.verticalGradient(listOf(colorResource(id = R.color.cursor_color),colorResource(id = R.color.cursor_color)))
            )

            if(text.value.isEmpty() && placeholder != null) {
                Text(
                    text = placeholder,
                    style = TextStyle.Default.copy(
                        color = Color.Gray
                    )
                )
            }
        }

        if(trailingIcon != null) {
            trailingIcon()
        }
    }

}


@Composable
fun GeneralButton(
    text : String,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(dimensionResource(id = R.dimen.halfGeneralPadding)),
    onClick : () -> Unit,
) {
    Button(
        onClick = {onClick()},
        modifier = Modifier
            .fillMaxWidth(),
        shape = shape
    ) {
        GeneralText(
            text,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.mediumPadding)),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun GeneralDropDownSelector(
    text: MutableState<String>,
    modifier: Modifier,
    dropDownList : List<String>,
    showDropDown : MutableState<Boolean>,
    placeholder: String? = null,
    keyboardOption: KeyboardOptions = KeyboardOptions.Default,
    content : @Composable BoxScope.() -> Unit
) {
    val transition = updateTransition(targetState = showDropDown.value, label = "rotate drop down arrow")
    val rotation = transition.animateFloat(label = "rotate drop down arrow") {
        if(it){
            180f
        } else {
            0f
        }
    }

    GeneralEditText(
        text = text,
        modifier = modifier,
        placeholder = placeholder,
        keyboardOption = keyboardOption,
        enabled = false,
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_drop_down),
                contentDescription = "drop down arrow",
                modifier = Modifier
                    .rotate(rotation.value),
                tint = colorResource(id = R.color.onBackground)
            )
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        content()

        AnimatedVisibility(visible = showDropDown.value) {
            GeneralDropDownList(
                contentList = dropDownList,
                onClick = { ind, string ->
                    text.value = string
                    showDropDown.value = false
                }
            )
        }
    }

}

@Composable
fun GeneralDropDownList(
    contentList : List<String> = listOf(""),
    onClick: (Int, String) -> Unit
) {

    val singleChildHeight = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val dp = with(density) {
        (5*singleChildHeight.intValue).toDp() + 2.dp
    }
    @Composable
    fun DropDownItem(
        itemName : String,
        onClick: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onClick()
                }
                .onGloballyPositioned {
                    singleChildHeight.intValue = it.size.height
                }
        ) {
            GeneralText(
                text = itemName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.halfGeneralPadding)),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = colorResource(id = R.color.onBackground),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat))
                )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(colorResource(id = R.color.onBackground))
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(dp)
            .padding(top = dimensionResource(id = R.dimen.normalPadding))
            .border(
                BorderStroke(0.1.dp, Color.Black),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.generalPadding))
            )
            .shadow(
                elevation = dimensionResource(id = R.dimen.halfGeneralPadding),
                shape = RoundedCornerShape(
                    dimensionResource(id = R.dimen.generalPadding)
                )
            )
            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.generalPadding)))
            .background(colorResource(id = R.color.background))
    ) {
        itemsIndexed(items = contentList, itemContent = {ind, string ->
            DropDownItem(itemName = string){
                onClick(ind, string)
            }
        })
    }
}