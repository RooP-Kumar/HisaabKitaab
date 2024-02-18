package com.example.hisaabkitaab.ui.screen.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.databinding.FragmentSignUpBinding
import com.example.hisaabkitaab.db.entity.User
import com.example.hisaabkitaab.ui.screen.home.HomeViewModel
import com.example.hisaabkitaab.ui.utility.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private val viewModel : HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        binding.signUpBtn.setOnClickListener {
            val email = binding.emailTV.text.toString()
            val pass = binding.passTV.text.toString()
            val phone = binding.phoneTV.text.toString()
            val user = User(email = email, phone = phone)
            viewModel.signUp(user, pass)
        }

        binding.signUpScreenText.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }
}
