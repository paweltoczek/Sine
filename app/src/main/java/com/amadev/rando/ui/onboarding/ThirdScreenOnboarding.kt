package com.amadev.rando.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.amadev.rando.R
import com.amadev.rando.databinding.FragmentSecondScreenOnboardingBinding
import com.amadev.rando.databinding.FragmentThirdScreenOnboardingBinding
import com.amadev.rando.util.Animations

class ThirdScreenOnboarding : Fragment() {

    private var _binding: FragmentThirdScreenOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_third_screen_onboarding, container, false)
        _binding = FragmentThirdScreenOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Animations.animateAlphaWithHandlerDelay(binding.stopTv, 700, 1.0f, 300)
        Animations.animateAlphaWithHandlerDelay(binding.browsingthewebTv, 700, 1.0f, 1000)
        Animations.animateAlphaWithHandlerDelay(binding.shuffleitTv, 1000, 1.0f, 2000)
        Animations.animateAlphaWithHandlerDelay(binding.finish, 1000, 1.0f, 2000)
        Animations.animateAlphaWithHandlerDelay(binding.previous, 1000, 1.0f, 2000)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.finish.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_signinOrSignUpFragment)
            onBoardingFinished()
        }

        binding.previous.setOnClickListener {
            viewPager?.currentItem = 1
        }


    }

    private fun onBoardingFinished() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val sharedPrefEditor = sharedPreferences.edit()
        sharedPrefEditor.putBoolean("Finished", true)
        sharedPrefEditor.apply()
    }
}