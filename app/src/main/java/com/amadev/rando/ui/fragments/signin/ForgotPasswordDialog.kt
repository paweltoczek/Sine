package com.amadev.rando.ui.fragments.signin

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.amadev.rando.R
import com.amadev.rando.databinding.ForgotPasswordDialogBinding
import com.amadev.rando.util.Util.showToast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordDialog() : DialogFragment() {
    private val auth = FirebaseAuth.getInstance()
    private var _binding: ForgotPasswordDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ForgotPasswordDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.sendEmailPasswordReset.setOnClickListener {
            binding.apply {
                if (usernameInput.text.trim().isEmpty()) {
                    usernameInput.error = getString(R.string.field_cannot_be_empty)
                } else if (Patterns.EMAIL_ADDRESS.matcher(usernameInput.text.toString()).matches()
                        .not()
                ) {
                    usernameInput.error = getString(R.string.pleaseEnterValidEmailAdress)
                } else {
                    sendResetPasswordEmail()
                }
            }
        }

        binding.closeDialogBtn.setOnClickListener {
            dialog!!.dismiss()
        }

    }

    private fun sendResetPasswordEmail() {

        auth.sendPasswordResetEmail(binding.usernameInput.text.toString())
            .addOnSuccessListener {
                showToast(requireContext(), getString(R.string.emailSent))
                dialog!!.dismiss()
            }
            .addOnFailureListener {
                binding.usernameInput.error = getString(R.string.failedToSendAnEmail)
            }
    }

}