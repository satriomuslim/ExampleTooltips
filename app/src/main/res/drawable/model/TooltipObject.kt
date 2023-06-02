package com.example.tooltipscontoh.model

import android.view.View
import android.view.ViewGroup
import com.example.exampletooltips.model.TooltipContentPosition

class TooltipObject(
    val view: View?,
    val title: String?,
    val text: String?,
    val tooltipContentPosition: TooltipContentPosition = TooltipContentPosition.UNDEFINED,
    val tintBackgroundColor: Int = 0,
    val scrollView: ViewGroup? = null
) {

    private var location: IntArray = IntArray(0)
    private var radius = 0

    fun getLocation(): IntArray? {
        return location
    }

    fun getRadius(): Int {
        return radius
    }
}