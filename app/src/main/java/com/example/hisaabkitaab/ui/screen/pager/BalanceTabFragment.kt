package com.example.hisaabkitaab.ui.screen.pager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.databinding.FragmentBalanceTablLayoutBinding
import com.example.hisaabkitaab.ui.component.GeneralText
import com.example.hisaabkitaab.ui.screen.myepense.MyExpense
import com.example.hisaabkitaab.ui.screen.myepense.MyExpenseViewModel
import com.example.hisaabkitaab.ui.screen.transaction.TransactionViewModel
import com.example.hisaabkitaab.ui.screen.transaction.Transactions
import com.example.hisaabkitaab.ui.utility.Utility
import com.example.hisaabkitaab.ui.utility.getBalanceTabs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BalanceTabFragment : Fragment() {
    private lateinit var binding: FragmentBalanceTablLayoutBinding
    private val myExpenseViewModel by viewModels<MyExpenseViewModel>()
    private val transactionViewModel by viewModels<TransactionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_balance_tabl_layout,
            container,
            false
        )
        val topAppBarAmountTv : TextView = requireActivity().findViewById(R.id.balance_total_price)
        binding.composeView.setContent {
            MainUI(topAppBarAmountTv)
        }
        return binding.root
    }

    @Composable
    private fun MainUI(
        topAppbarTotalAmountTv: TextView
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TabBodyLayout(topAppbarTotalAmountTv)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun TabBodyLayout(
        topAppbarTotalAmountTv: TextView
    ) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(0) {
            2
        }
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(id = R.color.background)
        ) {
            getBalanceTabs().forEachIndexed { page, tab ->
                Tab(
                    selected = pagerState.currentPage ==  page,
                    text = {
                        GeneralText(
                            tab.title,
                            style = TextStyle().copy(
                                color = colorResource(id = R.color.onBackground)
                            )
                        )
                    },
                    icon = {

                        Icon(
                            painter = painterResource(id = tab.icon),
                            contentDescription = tab.title,
                            modifier = Modifier
                                .size(20.dp),
                            tint = colorResource(id = R.color.onBackgroundTabIconTint)
                        )

                    },
                    onClick = { 
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            pageSpacing = 14.dp
        ) {
            when (it) {
                0 -> {
                    MyExpense(myExpenseViewModel, topAppbarTotalAmountTv)
                }

                1 -> {
                    Transactions(transactionViewModel, topAppbarTotalAmountTv)
                }
            }
        }
    }

}
