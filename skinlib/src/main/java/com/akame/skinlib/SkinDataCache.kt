package com.akame.skinlib

import android.content.Context
import android.content.SharedPreferences

internal object SkinDataCache {
    private lateinit var spf: SharedPreferences
    fun init(context: Context) {
        spf = context.getSharedPreferences("SKIN_APP", Context.MODE_PRIVATE)
    }

    fun setSkinPath(skinPath: String) {
        spf.edit().putString("skinPath", skinPath).apply()
    }

    fun getSkinPath() = spf.getString("skinPath", "") ?: ""

    fun reset() = spf.edit().remove("skinPath").apply()
}