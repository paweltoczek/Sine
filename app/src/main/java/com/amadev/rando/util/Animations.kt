package com.amadev.rando.util

import android.os.Handler
import android.view.View


object Animations {

    fun animateAlphaWithHandlerDelay(property: View, animationTime: Long, targetAlpha: Float, delay: Long) {
        Handler().postDelayed({
            property.animate().apply {
                duration = (animationTime)
                alpha(targetAlpha)
            }
        }, delay)
    }

    fun animationTravelYWithAlpha(property: View, animationTime: Long, targetY: Float, alpha : Float) {
        property.animate().apply {
            duration = (animationTime)
            translationY(targetY)
            alpha(alpha)
        }
    }

    fun scaleXY(property: View, animationTime: Long, scale : Float ) {
        property.animate().apply {
            duration = (animationTime)
            scaleX(scale)
            scaleY(scale)
        }
    }

    fun rotateAnimation(view : View, rotationDegree : Float, animationTime : Long) {
        view.animate().apply {
            duration = animationTime
            rotation(rotationDegree)
        }
    }





}