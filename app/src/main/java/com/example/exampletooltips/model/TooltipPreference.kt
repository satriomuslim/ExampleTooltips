package com.example.exampletooltips.model

import android.content.Context
import android.content.SharedPreferences

object TooltipPreference {
    private val UGLY_TOOLTIP_PREF = "ugly_tooltip_pref"

//    fun hasShown(context: Context, tag: String?): Boolean {
//        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
//            UGLY_TOOLTIP_PREF,
//            Context.MODE_PRIVATE
//        )
//        return sharedPreferences.getBoolean(tag, false)
//    }

    fun setShown(context: Context, tag: String?, hasShown: Boolean) {
        val sharedPreferencesEditor: SharedPreferences.Editor = context.getSharedPreferences(
            UGLY_TOOLTIP_PREF,
            Context.MODE_PRIVATE
        ).edit()
        sharedPreferencesEditor.putBoolean(tag, hasShown)
        sharedPreferencesEditor.apply()
    }
}