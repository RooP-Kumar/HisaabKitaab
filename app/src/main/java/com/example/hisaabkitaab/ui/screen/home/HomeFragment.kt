package com.example.hisaabkitaab.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.databinding.FragmentHomeBinding
import com.example.hisaabkitaab.ui.utility.Utility
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        mainUI()
        return binding.root

    }

    private fun mainUI() {

        binding.addMyExpenseBtn.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addMyExpenseFragment)
        }

        binding.addTransactionBtn.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addTransactionDialogFragment)
        }
    }

}