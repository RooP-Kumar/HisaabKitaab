package com.example.hisaabkitaab.ui.screen.myepense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.adapter.MyExpenseRecyclerViewAdapter
import com.example.hisaabkitaab.databinding.FragmentMyExpenseBinding
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.ui.utility.main
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyExpenseFragment : Fragment() {
    private lateinit var binding : FragmentMyExpenseBinding
    private val viewModel : MyExpenseViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_expense, container, false)
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        val adapter = MyExpenseRecyclerViewAdapter(requireContext())
        val toolbarTotalAmountTv : TextView = requireActivity().findViewById(R.id.balance_total_price)
        toolbarTotalAmountTv.text = requireContext().getString(R.string.rupee,
            "0.0")
        binding.myExpenseRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        binding.myExpenseRecyclerView.adapter = adapter
        main {
            viewModel.currentMonthExpenses.collectLatest {
                if(it.isEmpty()) {
                    toolbarTotalAmountTv.text = requireContext().getString(R.string.rupee,
                        "0.0")
                    binding.myExpenseEmptyAnimation.visibility = View.VISIBLE
                } else {
                    binding.myExpenseEmptyAnimation.visibility = View.GONE
                    val totalAmount = getTotalExpenseAmount(it)
                    toolbarTotalAmountTv.text = requireContext().getString(R.string.rupee,
                        totalAmount.toDouble().toString())
                    adapter.updateList(it)
                }
            }
        }
    }

    private fun getTotalExpenseAmount(myExpenses : List<MyExpense>) : Long {
        var totalPrice = 0L
        myExpenses.forEach {
            totalPrice += it.price!!
        }
        return totalPrice
    }
}