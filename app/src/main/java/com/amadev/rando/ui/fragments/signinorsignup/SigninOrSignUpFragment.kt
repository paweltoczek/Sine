package com.amadev.rando.ui.fragments.signinorsignup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amadev.rando.R
import kotlinx.android.synthetic.main.fragment_signin_or_sign_up.*

class SigninOrSignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin_or_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
}