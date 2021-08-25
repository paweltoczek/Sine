package com.amadev.rando.ui.dialogs.forgotPassword

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.amadev.rando.databinding.ForgotPasswordDialogBinding
import com.amadev.rando.util.Util.showToast
import org.koin.android.viewmodel.ext.android.viewModel

class ForgotPasswordDialog : DialogFragment() {

    private var _binding: ForgotPasswordDialogBinding? = null
    private val binding get() = _binding!!
    private val forgotPasswordDialogViewModel: ForgotPasswordDialogViewModel by viewModel()

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

        setDialogBackgroundColor()
        setUpObservers()
        setUpOnClickListeners()

    }

    private fun setUpOnClickListeners() {
        binding.apply {
            sendEmailPasswordReset.setOnClickListener {
                validateInput(binding.usernameInput)
            }
            binding.closeDialogBtn.setOnClickListener {
                dialog!!.dismiss()
            }
        }
    }

    private fun setDialogBackgroundColor() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setUpObservers() {
        forgotPasswordDialogViewModel.apply {
            popUpMessageLiveData.observe(viewLifecycleOwner) {
                showToast(requireContext(), it)
            }
            dialogDismissMutableLiveData.observe(viewLifecycleOwner) {
                if (it == true) dialog?.dismiss()
            }
        }
    }

    private fun validateInput(username: EditText) {
        forgotPasswordDialogViewModel.validateInput(username.text.toString())
    }


}