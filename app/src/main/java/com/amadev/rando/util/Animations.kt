package com.amadev.rando.util

import android.os.Handler
import android.view.View


object Animations {

    fun animateAlphaWithHandlerDelay(property : View, time : Long, targetAlpha : Float, delay : Long) {
        Handler().postDelayed ({
        property.animate().apply {
            duration = (500)
            alpha(targetAlpha)}
        },delay)
    }
}