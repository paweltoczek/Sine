package com.amadev.rando.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.amadev.rando.R
import com.amadev.rando.databinding.FragmentSecondScreenOnboardingBinding
import com.amadev.rando.util.Animations

class SecondScreenOnboarding : Fragment() {

    private var _binding: FragmentSecondScreenOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_second_screen_onboarding, container, false)
        _binding = FragmentSecondScreenOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Animations.animateAlphaWithHandlerDelay(binding.ittakesTv, 700, 1.0f, 300)
        Animations.animateAlphaWithHandlerDelay(binding.longTv, 700, 1.0f, 1000)
        Animations.animateAlphaWithHandlerDelay(binding.soTv, 1000, 1.0f, 1500)
        Animations.animateAlphaWithHandlerDelay(binding.next, 1000, 1.0f, 1500)
        Animations.animateAlphaWithHandlerDelay(binding.previous, 1000, 1.0f, 1500)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.previous.setOnClickListener {
            viewPager?.currentItem = 0
        }
    }


}