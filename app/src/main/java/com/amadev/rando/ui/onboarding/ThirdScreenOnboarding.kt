package com.amadev.rando.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.amadev.rando.R
import com.amadev.rando.util.Animations
import kotlinx.android.synthetic.main.fragment_third_screen_onboarding.*

class ThirdScreenOnboarding : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third_screen_onboarding, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Animations.animateAlphaWithHandlerDelay(stop_tv, 700, 1.0f, 300)
        Animations.animateAlphaWithHandlerDelay(browsingtheweb_tv, 700, 1.0f, 1000)
        Animations.animateAlphaWithHandlerDelay(shuffleit_tv, 1000, 1.0f, 2000)
    }
}