package com.akame.skinlib

import android.R
import android.app.Activity
import android.content.Context
import android.os.Build

internal object SkinThemeUtils {
    //APP设置的主题
    private val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(
        R.attr.colorPrimaryDark
    )

    //需要找到哪些系统属性的值
    private val STATUSBAR_COLOR_ATTRS = intArrayOf(
        R.attr.statusBarColor, R.attr.navigationBarColor
    )

    /**
     * 获取系统主题的资源id
     */
    fun getResId(context: Context, attrs: IntArray): IntArray {
        val resIds = IntArray(attrs.size)
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs)
        for (i in attrs.indices) {
            resIds[i] = obtainStyledAttributes.getResourceId(i, 0)
        }
        obtainStyledAttributes.recycle()
        return resIds
    }

    /**
     * 更新系统状态栏颜色
     */
    fun updateStatusBarColor(activity: Activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val resId = getResId(activity, STATUSBAR_COLOR_ATTRS)
        val statusBarColor = resId[0]
        val navigationBarColor = resId[1]
        if (statusBarColor != 0) {
            val color = SkinResourcesUtils.getColor(statusBarColor)
            activity.window.statusBarColor = color
        } else {
            val colorPrimaryDarkResId: Int =
                getResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            if (colorPrimaryDarkResId != 0) {
                val color = SkinResourcesUtils.getColor(colorPrimaryDarkResId)
                activity.window.statusBarColor = color
            }
        }
        if (navigationBarColor != 0) {
            val color = SkinResourcesUtils.getColor(navigationBarColor)
            activity.window.navigationBarColor = color
        }
    }
}