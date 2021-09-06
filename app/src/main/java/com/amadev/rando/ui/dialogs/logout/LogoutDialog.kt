package com.amadev.rando.ui.dialogs.logout

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.amadev.rando.R
import com.amadev.rando.databinding.LogoutDialogBinding
import com.amadev.rando.util.Util.showToast
import org.koin.android.viewmodel.ext.android.viewModel

class LogoutDialog : DialogFragment() {

    private var _binding: LogoutDialogBinding? = null
    private val binding get() = _binding!!
    private val logoutDialogViewModel: LogoutDialogViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LogoutDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackgroundWindowColor()
        setUpOnClickListeners()
        setUpObservers()

    }

    private fun setUpOnClickListeners() {
        binding.apply {
            logoutBtn.setOnClickListener {
                logOut()
                closeDialog()
                navigateToWelcomeScreen()

            }

            closeDialogBtn.setOnClickListener {
                closeDialog()
            }
        }
    }

    private fun setBackgroundWindowColor() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun navigateToWelcomeScreen() {
//        findNavController().navigate(R.id.action_choiceFragment_to_welcomeScreen)
    }

    private fun logOut() {
        logoutDialogViewModel.logOut()
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun setUpObservers() {
        logoutDialogViewModel.apply {
            popUpMessageLiveData.observe(viewLifecycleOwner) {
                showToast(requireContext(), it)
            }
        }
    }
}