package com.example.hisaabkitaab.ui.screen.addexpense

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.adapter.AddExpenseItemsRecyclerViewAdapter
import com.example.hisaabkitaab.databinding.FragmentAddMyExpenseBinding
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.ui.screen.dialog.AddExpenseAddItemDialog
import com.example.hisaabkitaab.ui.utility.FragmentDialog
import com.example.hisaabkitaab.ui.utility.MY_EXPENSE_TITLE_AMOUNT_SEPARATOR
import com.example.hisaabkitaab.ui.utility.Utility
import com.example.hisaabkitaab.ui.utility.fetchAmount
import com.example.hisaabkitaab.ui.utility.getRandomId
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class AddMyExpenseFragment : Fragment() {
    private lateinit var binding: FragmentAddMyExpenseBinding
    private val viewModel: AddExpenseViewModel by viewModels()
    private var dialog: FragmentDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_my_expense, container,
            false
        )
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        val adapter = AddExpenseItemsRecyclerViewAdapter(requireContext())
        binding.addExpenseItemsRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )

        binding.addExpenseItemsRecyclerView.adapter = adapter
        viewModel.allMyExpense.observe(viewLifecycleOwner) {
            it?.let { myExpense1 ->
                if (myExpense1.isNotEmpty()) {
                    binding.addExpenseNoItemsTV.visibility = View.GONE
                    binding.addExpenseItemsRecyclerView.visibility = View.VISIBLE
                } else {
                    binding.addExpenseItemsRecyclerView.visibility = View.GONE
                    binding.addExpenseNoItemsTV.visibility = View.VISIBLE
                }
                adapter.updateList(myExpense1)
            }
        }

        binding.addExpenseAddItemBtn.setOnClickListener { _ ->
            dialog = FragmentDialog(dismissOnOutsideClick = false)
            dialog!!.show(requireActivity().supportFragmentManager, "add expense dialog")
        }

        binding.addExpenseAddExpenseBtn.setOnClickListener {
            var totalAmount = 0L
            val temp = viewModel.allMyExpense.value
            temp?.forEach {
                totalAmount += it.fetchAmount()
            }
            if (temp != null) {
                viewModel.insertExpense(
                    MyExpense(
                        id = getRandomId(), price = totalAmount, items = temp.toList(),
                        Date(System.currentTimeMillis())
                    )
                )
                viewModel.clearList()
            }
        }
    }

}
