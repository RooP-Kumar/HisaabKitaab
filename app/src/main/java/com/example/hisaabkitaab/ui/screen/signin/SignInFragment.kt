package com.example.hisaabkitaab.ui.screen.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.hisaabkitaab.R
import com.example.hisaabkitaab.databinding.FragmentSignInBinding
import com.example.hisaabkitaab.ui.screen.home.HomeViewModel
import com.example.hisaabkitaab.ui.utility.FragmentDialog
import com.example.hisaabkitaab.ui.utility.LoadingState
import com.example.hisaabkitaab.ui.utility.Utility
import com.example.hisaabkitaab.ui.utility.main
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SignInFragment : Fragment() {
    lateinit var binding : FragmentSignInBinding
    private val viewModel : HomeViewModel by viewModels()
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        mainUI()
        return binding.root
    }

    private fun mainUI() {
        val dialog = FragmentDialog()
        binding.signInBtn.setOnClickListener {
            val email = binding.emailTV.text.toString()
            val pass = binding.passTV.text.toString()

            viewModel.signIn(email, pass)
        }

        viewModel.loading.observe(viewLifecycleOwner) { loadingState ->
            when(loadingState) {
                LoadingState.IDLE -> {

                }
                LoadingState.LOADING -> {
                    dialog.show(requireActivity().supportFragmentManager, "Sign in login dialog")
                }
                LoadingState.SUCCESS -> {
                    main {
                        delay(1300)
                        dialog.dismiss()
                        findNavController().navigate(R.id.action_signInFragment_to_home_nav)
                    }
                }
                LoadingState.FAILURE -> {
                    dialog.dismiss()
                }
                else -> {}
            }
        }

        binding.loginScreenText.setOnClickListener{
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_signInFragment_to_home_nav)
        }
    }
}