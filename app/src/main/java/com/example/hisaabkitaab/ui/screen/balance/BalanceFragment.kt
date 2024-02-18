package com.example.hisaabkitaab.ui.screen.balance

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
import com.example.hisaabkitaab.adapter.ShowTransactionRecyclerViewAdapter
import com.example.hisaabkitaab.databinding.FragmentBalanceBinding
import com.example.hisaabkitaab.ui.utility.Utility
import com.example.hisaabkitaab.ui.utility.main
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceFragment : Fragment() {
    private lateinit var binding : FragmentBalanceBinding
    private val viewModel : BalanceViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance, container, false)

        mainUI()
        return binding.root
    }

    private fun mainUI() {
        val toolbarTotalAmount : TextView = requireActivity().findViewById(R.id.balance_total_price)
        toolbarTotalAmount.text = requireContext().getString(R.string.rupee, "0.0/0.0")
        binding.showTransactionsRV.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        val adapter = ShowTransactionRecyclerViewAdapter(requireContext()) {friend, trans->
            viewModel.updateTransaction(trans, friend)
        }
        binding.showTransactionsRV.adapter = adapter

        main {
            viewModel.allTransaction.observe(viewLifecycleOwner) {
                if(it.isEmpty()) {
                    binding.emptyBucketAnimation.visibility = View.VISIBLE
                    toolbarTotalAmount.text = requireContext().getString(R.string.rupee, "0.0/0.0")
                } else {
                    binding.emptyBucketAnimation.visibility = View.GONE
                    toolbarTotalAmount.text = requireContext().getString(R.string.rupee, "0.0/0.0")
                }
                adapter.updateList(it)
            }
        }
    }
}