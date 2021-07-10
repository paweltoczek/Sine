package com.amadev.rando.ui.fragments.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.amadev.rando.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {

    private lateinit var signInViewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.let {
            signInViewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        }

        setUpViewModel()
        setUpObservers()
        setUpOnBackPressedCallback()
        setUpOnClickListeners()

    }

    private fun setUpViewModel() {
        signInViewModel.apply {
            loginAutomaticallyIfPossible()
        }
    }

    private fun setUpOnClickListeners() {
        signin_btn.setOnClickListener {
            signInViewModel.validateInput(
                username_input.text.toString(),
                password_input.text.toString())
        }
    }


    private fun setUpObservers() {
        signInViewModel.apply {
            usernameInputErrorLiveData.observe(viewLifecycleOwner) {
                username_input.error = it
            }
            invalidUsernameLiveData.observe(viewLifecycleOwner) {
                username_input.error = it
            }
            passwordInputErrorLiveData.observe(viewLifecycleOwner) {
                password_input.error = it
            }
            messageLiveData.observe(viewLifecycleOwner) {
                val snack = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
                snack.show()
                username_input.text.clear()
                password_input.text.clear()
            }
            setUpProgressbarVisibility.observe(viewLifecycleOwner) {
                if (it == true) progressbar.visibility = View.VISIBLE
                else progressbar.visibility = View.GONE
            }
            loginSuccessfulLiveData.observe(viewLifecycleOwner) {
                if (it == true) findNavController().navigate(R.id.action_signInFragment_to_choiceFragment)
            }
            loginAutomaticallyIfPossibleLiveData.observe(viewLifecycleOwner) {
                if (it == true) findNavController().navigate(R.id.action_signInFragment_to_choiceFragment)
            }
        }
    }

    private fun setUpOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_signInFragment_to_signinOrSignUpFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }


}