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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_signin_or_sign_up.*

class SigninOrSignUpFragment : Fragment() {

    private lateinit var signInOrSignUpViewModel: SignInOrSignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_signin_or_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.let {
            signInOrSignUpViewModel =
                ViewModelProviders.of(this).get(SignInOrSignUpViewModel::class.java)
        }

        setUpObservers()
        setUpViewModel()
        setUpOnBackPressedCallback()
        setUpOnClickListeners()

    }

    private fun setUpObservers() {
        signInOrSignUpViewModel.apply {
            loginAutomaticallyIfPossibleLiveData.observe(viewLifecycleOwner) {
                if (it == true) {
                    findNavController().navigate(R.id.action_signinOrSignUpFragment_to_choiceFragment)
                }
            }
            messageLiveData.observe(viewLifecycleOwner) {
                val snack = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
                snack.show()
            }
        }

    }

    private fun setUpViewModel() {
        signInOrSignUpViewModel.apply {
            loginAutomaticallyIfPossible()
        }
    }

    private fun setUpOnClickListeners() {
        skip_btn.setOnClickListener {
            findNavController().navigate(R.id.action_signinOrSignUpFragment_to_choiceFragment)
        }

        signin_btn.setOnClickListener {
            findNavController().navigate(R.id.action_signinOrSignUpFragment_to_signInFragment)
        }

        signup_btn.setOnClickListener {
            findNavController().navigate(R.id.action_signinOrSignUpFragment_to_signUpFragment)
        }
    }

    private fun setUpOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}