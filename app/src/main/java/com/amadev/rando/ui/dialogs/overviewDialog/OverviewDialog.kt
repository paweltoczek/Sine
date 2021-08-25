package com.amadev.rando.ui.dialogs.overviewDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.amadev.rando.databinding.OverviewDialogBinding

class OverviewDialog(private val overviewText: String) : DialogFragment() {

    private var _binding: OverviewDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OverviewDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackgroundColor()
        setTextViewVerticalScrollable(binding.overviewDialogText)
        setOverviewText(overviewText)
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        binding.apply {
            closeDialogBtn.setOnClickListener {
                closeDialog()
            }
        }
    }

    private fun setTextViewVerticalScrollable(textView: TextView) {
        textView.movementMethod = ScrollingMovementMethod.getInstance()
    }

    private fun setOverviewText(text: String) {
        binding.overviewDialogText.text = text
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun setBackgroundColor() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}