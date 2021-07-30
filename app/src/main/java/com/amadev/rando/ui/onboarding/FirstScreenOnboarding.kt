package com.amadev.rando.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.amadev.rando.R
import com.amadev.rando.databinding.FragmentFirstScreenOnboardingBinding
import com.amadev.rando.databinding.FragmentSignUpBinding
import com.amadev.rando.util.Animations.animateAlphaWithHandlerDelay

class FirstScreenOnboarding : Fragment() {
    private var _binding: FragmentFirstScreenOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_first_screen_onboarding, container, false)
        _binding = FragmentFirstScreenOnboardingBinding.inflate(inflater, container, false)
        return binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateAlphaWithHandlerDelay(binding.howlongdoes,1000,1.0f,700)
        animateAlphaWithHandlerDelay(binding.next,1000,1.0f,1500)



        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        binding.next.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }

}