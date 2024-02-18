package com.example.hisaabkitaab.ui.screen.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.databinding.FragmentSettingBinding
import com.example.hisaabkitaab.ui.component.GeneralDialog
import com.example.hisaabkitaab.ui.utility.Constants
import com.example.hisaabkitaab.ui.utility.LoadingState
import com.example.hisaabkitaab.ui.utility.SyncingState
import com.example.hisaabkitaab.ui.utility.Utility
import dagger.hilt.android.AndroidEntryPoint

data class SettingUiState (
    val syncState : MutableState<SyncingState> = mutableStateOf(SyncingState.IDLE),
    val syncMsg : MutableState<String?> = mutableStateOf(null),
    val loadingState : MutableState<LoadingState> = mutableStateOf(LoadingState.IDLE)
)

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel : SettingViewModel by viewModels()

    companion object {
        const val TAG = "Setting Fragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.composeView.setContent {
            MainUI()
        }
        return binding.root
    }

    @Composable
    private fun MainUI() {
        val uiState = viewModel.settingUiState
        val context = LocalContext.current

        LaunchedEffect(key1 = uiState.loadingState.value, block = {
            when(uiState.loadingState.value) {
                LoadingState.LOADING -> {}
                LoadingState.SUCCESS -> { findNavController().clearBackStack(R.id.auth_nav); findNavController().navigate(R.id.auth_nav) }
                LoadingState.FAILURE -> {}
                LoadingState.IDLE -> {}
            }
        })

        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.background))
        ) {
            val itemColor : Color = colorResource(id = R.color.onBackground)
            if(uiState.syncMsg.value != null) {
                Toast.makeText(context, uiState.syncMsg.value, Toast.LENGTH_SHORT).show()
            }
            SyncSettingRow(viewModel, itemColor)
            SettingLogout(viewModel, itemColor)
            GeneralDialog(
                uiState.loadingState
            )

            }
        }
    }

@Composable
fun SyncSettingRow(
    viewModel: SettingViewModel,
    itemColor : Color
) {
    val btnClicked : MutableState<Boolean> = remember { mutableStateOf(false) }
    val rotationValue : MutableFloatState = remember { mutableFloatStateOf(360f) }

    val transition = updateTransition(targetState = btnClicked, label = "rotate 360 degree")

    val rotation = transition.animateFloat (
        transitionSpec = {
            tween(800)
        },
        label = "rotating image"
    ) {
        if(it.value) {
            360f// when value is true ->  returning 0 - 360
        } else {
            0f // when value is false -> returning 360 - 0
        }
    }

    // Rotation logic when click on a button rotation should be on opposite direction.
    fun callbackForRotation() : Float {
        return rotationValue.floatValue - if(btnClicked.value) rotation.value else -1*rotation.value // handling float value from 0 - 360 and 360 - 0
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                btnClicked.value = !btnClicked.value
                viewModel.syncData()
            }
            .padding(top = dimensionResource(id = R.dimen.halfGeneralPadding))
            .padding(
                horizontal = dimensionResource(id = R.dimen.generalPadding),
                vertical = dimensionResource(id = R.dimen.halfGeneralPadding)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Sync with database",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontSize = 14.sp,
                letterSpacing = 0.1.sp,
                color = itemColor
            )
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_sync),
            contentDescription = "Sync icon",
            tint = itemColor,
            modifier = Modifier
                .rotate(callbackForRotation()) // callbackForRotation function returning float value with handling true and false value of btnClicked state
        )
    }


}


@Composable
fun SettingLogout(
    viewModel: SettingViewModel,
    itemColor: Color
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.logout()
            }
            .padding(top = dimensionResource(id = R.dimen.halfGeneralPadding))
            .padding(
                horizontal = dimensionResource(id = R.dimen.generalPadding),
                vertical = dimensionResource(id = R.dimen.halfGeneralPadding)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Logout",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontSize = 14.sp,
                letterSpacing = 0.1.sp,
                color = itemColor
            )
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_sync),
            contentDescription = "Sync icon",
            tint = itemColor
        )
    }
}



