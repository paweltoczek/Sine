package com.amadev.rando.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.amadev.rando.R
import kotlinx.android.synthetic.main.fragment_first_screen_onboarding.view.*


class FirstScreenOnboarding : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first_screen_onboarding, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        view.next_btn.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}