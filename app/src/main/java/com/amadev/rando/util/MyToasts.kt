package com.amadev.rando.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object MyToasts {

    enum class Position(val value: Int) {
        TOP(Gravity.TOP),
        CENTER(Gravity.CENTER),
        BOTTOM(Gravity.BOTTOM)
    }

    fun myToast(
        baseContext: Context?,
        text: String,
        position: Position = Position.CENTER,
        posX: Int = 0,
        posY: Int = 0
    ) {

        Toast.makeText(baseContext, text, Toast.LENGTH_SHORT).apply {
            setGravity(position.value, posX, posY)
            show()
        }
    }


}