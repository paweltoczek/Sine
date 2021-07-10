package com.amadev.rando.ui.fragments.signinorsignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.amadev.rando.R
import com.amadev.rando.ui.fragments.signup.SignUpViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : Fragment() {

    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.let {
            signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        }

        setUpProgressBarVisibility()
        setUpObservers()
        setUpOnClickListeners()
        setUponBackPressedCallback()

    }

    private fun setUpOnClickListeners() {
        signup_btn.setOnClickListener {
            sendInputsToViewModel()
        }
    }

    private fun setUpProgressBarVisibility() {
        signUpViewModel.progressBarVisibleLiveData.observe(viewLifecycleOwner) {
            if (it == true) progress_bar.visibility = View.VISIBLE else {
                progress_bar.visibility = View.GONE
            }
        }
    }

    private fun sendInputsToViewModel() {
        signUpViewModel.apply {
            validateInput(
                username_input.text.toString(),
                password_input.text.toString(),
                password_repeat_input.text.toString()
            )
        }
    }

    private fun setUpObservers() {
        signUpViewModel.apply {
            usernameInputErrorLiveData.observe(viewLifecycleOwner) {
                username_input.error = it
            }
            invalidUsernameLiveData.observe(viewLifecycleOwner) {
                username_input.error = it
            }
            passwordInputErrorLiveData.observe(viewLifecycleOwner) {
                password_input.error = it
            }
            emptyRepeatPasswordInputLiveData.observe(viewLifecycleOwner) {
                password_repeat_input.error = it
            }
            passwordsAreNotTheSameLiveData.observe(viewLifecycleOwner) {
                password_repeat_input.error = it
            }
            toastTextLiveData.observe(viewLifecycleOwner) {
                val snack = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
                snack.show()
                username_input.text.clear()
                password_input.text.clear()
                password_repeat_input.text.clear()
            }
            accountSuccessfullyCreatedLiveData.observe(viewLifecycleOwner) {
                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
            }
        }
    }

    private fun setUponBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signUpFragment_to_signinOrSignUpFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}