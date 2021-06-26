package com.amadev.rando.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.amadev.rando.R

import kotlinx.android.synthetic.main.fragment_view_pager.*
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf(
            FirstScreenOnboarding(),
            SecondScreenOnboarding(),
            ThirdScreenOnboarding()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = adapter

        val wormDotsIndicator = dots_indicator
        val viewPager = viewPager
        wormDotsIndicator.setViewPager2(viewPager)
    }

}