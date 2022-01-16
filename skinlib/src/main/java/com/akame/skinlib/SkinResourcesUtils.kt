package com.akame.skinlib

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable

/**
 * 资源管理器
 */
internal object SkinResourcesUtils {
    //App的资源管理器
    private lateinit var appResources: Resources

    //皮肤包的资源管理器
    private lateinit var skinResources: Resources

    //是否使用默认的皮肤
    private var isDefaultSkin = true

    //皮肤包路径
    private lateinit var skinPackageName: String

    /**
     * 设置默认值参数
     */
    fun apply(
        appResources: Resources,
        skinResources: Resources?,
        skinPackageName: String?
    ) {
        this.appResources = appResources
        skinResources?.let { this.skinResources = it }
        skinPackageName?.let { this.skinPackageName = it }
        this.isDefaultSkin = skinPackageName.isNullOrEmpty() || skinResources == null
    }

    /**
     * 重置参数
     */
    fun rest() {
        isDefaultSkin = true
    }

    /**
     * 获取资源包对应的颜色
     */
    fun getColor(colorId: Int): Int {
        val identifier = getIdentifier(colorId)
        return when {
            isDefaultSkin || identifier == 0 -> appResources.getColor(colorId)
            else -> skinResources.getColor(colorId)
        }
    }

    fun getColorStateList(resId: Int): ColorStateList {
        val identifier = getIdentifier(resId)
        return when {
            isDefaultSkin || identifier == 0 -> appResources.getColorStateList(resId)
            else -> skinResources.getColorStateList(resId)
        }
    }

    fun getDrawable(drawableId: Int): Drawable {
        val identifier = getIdentifier(drawableId)
        return when {
            isDefaultSkin || identifier == 0 -> appResources.getDrawable(drawableId)
            else -> skinResources.getDrawable(drawableId)
        }
    }

    /**
     * 获取背景资源
     */
    fun getBackground(resId: Int): Any {
        val resourceTypeName = appResources.getResourceTypeName(resId)
        return if (resourceTypeName == "color") {
            getColor(resId)
        } else {
            getDrawable(resId)
        }
    }


    /**
     * 获取资源id
     */
    private fun getIdentifier(resourcesId: Int): Int {
        if (isDefaultSkin) {
            return resourcesId
        }
        val resName = appResources.getResourceEntryName(resourcesId)
        val resTypeName = appResources.getResourceTypeName(resourcesId)
        return skinResources.getIdentifier(resName, resTypeName, skinPackageName)
    }
}