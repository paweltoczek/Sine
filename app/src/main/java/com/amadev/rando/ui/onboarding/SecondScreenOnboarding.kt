package com.amadev.rando.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.amadev.rando.R
import kotlinx.android.synthetic.main.fragment_second_screen_onboarding.view.*

class SecondScreenOnboarding : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second_screen_onboarding, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.back_btn.setOnClickListener {
            viewPager?.currentItem = 0
        }

        view.enjoy_btn.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_choiceFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}