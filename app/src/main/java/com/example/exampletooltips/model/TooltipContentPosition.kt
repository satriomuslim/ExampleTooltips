package com.example.exampletooltips.model

enum class TooltipContentPosition(position: Int) {
    UNDEFINED(0), TOP(1), RIGHT(2), BOTTOM(3), LEFT(4);

    private val position: Int = position
    fun getPosition(): Int {
        return position
    }

}