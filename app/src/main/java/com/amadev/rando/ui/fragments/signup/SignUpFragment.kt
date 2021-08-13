package com.amadev.rando.ui.fragments.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amadev.rando.R
import com.amadev.rando.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val signUpViewModel: SignUpViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        return inflater.inflate(R.layout.fragment_sign_up, container, false)
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpProgressBarVisibility()
        setUpObservers()
        setUpOnClickListeners()
        setUponBackPressedCallback()

    }

    private fun setUpOnClickListeners() {
        binding.signupBtn.setOnClickListener {
            sendInputsToViewModel()
        }
    }

    private fun setUpProgressBarVisibility() {
        signUpViewModel.progressBarVisibleLiveData.observe(viewLifecycleOwner) {
            if (it == true) binding.progressBar.visibility = View.VISIBLE else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun sendInputsToViewModel() {
        signUpViewModel.apply {
//            validateInput(
//                username_input.text.toString(),
//                password_input.text.toString(),
//                password_repeat_input.text.toString()
//            )
        }
    }

    private fun setUpObservers() {
        signUpViewModel.apply {
            usernameInputErrorLiveData.observe(viewLifecycleOwner) {
                binding.usernameInput.error = it
            }
            invalidUsernameLiveData.observe(viewLifecycleOwner) {
                binding.usernameInput.error = it
            }
            passwordInputErrorLiveData.observe(viewLifecycleOwner) {
                binding.passwordInput.error = it
            }
            emptyRepeatPasswordInputLiveData.observe(viewLifecycleOwner) {
                binding.passwordRepeatInput.error = it
            }
            passwordsAreNotTheSameLiveData.observe(viewLifecycleOwner) {
                binding.passwordRepeatInput.error = it
            }
            toastTextLiveData.observe(viewLifecycleOwner) {
                val snack = Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG)
                snack.show()
                binding.usernameInput.text.clear()
                binding.passwordInput.text.clear()
                binding.passwordRepeatInput.text.clear()
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