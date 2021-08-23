package com.amadev.rando.ui.dialogs.castDetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.amadev.rando.R
import com.amadev.rando.databinding.CastDetailsDialogBinding
import com.amadev.rando.model.CastModelResults
import com.amadev.rando.util.Util.getProgressDrawable
import com.amadev.rando.util.Util.loadImageWithGlide
import org.koin.android.viewmodel.ext.android.viewModel

class CastDetailsDialog(private val castDetailsList : CastModelResults) :
    DialogFragment() {

    private var _binding: CastDetailsDialogBinding? = null
    private val binding get() = _binding!!
//    private val castDetailsViewModel : CastDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CastDetailsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpWindowBackground()
        setUpOnClickListeners()
        setCastDetailsToTextViews()

    }

    private fun setUpOnClickListeners() {
        binding.apply {
            closeDialogBtn.setOnClickListener {
                closeDialog()
            }
        }
    }

    private fun closeDialog() {
        dialog?.dismiss()
    }

    private fun setUpWindowBackground() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun setCastDetailsToTextViews(){
        val castCharacter = getString(R.string.character) + ":" + " " + castDetailsList.character

        binding.apply {
            actorName.text = castDetailsList.name
            actorCharacter.text = castCharacter
            actorImage.loadImageWithGlide(
                castDetailsList.profile_path,
                getProgressDrawable(requireContext())
            )
        }
    }
}