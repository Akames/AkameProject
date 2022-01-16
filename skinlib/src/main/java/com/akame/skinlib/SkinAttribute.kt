package com.akame.skinlib

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat

internal class SkinAttribute {
    //将需要换肤的view保存起来
    private val mSkinViewList = arrayListOf<SkinView>()

    /**
     * 解析这个view的属性是否匹配有换肤的属性
     */
    fun look(view: View, attributeSet: AttributeSet) {
        val skinPairList = arrayListOf<SkinView.SkinPair>()
        for (i in 0 until attributeSet.attributeCount) {
            val attributeName = attributeSet.getAttributeName(i)
            if (mAttributeNameList.contains(attributeName)) {
                val attributeValue = attributeSet.getAttributeValue(i)
                // 以#开头的属性这种，值是在xml写死的没有对应的id 无法修改 比如#FFFFFF
                if (attributeValue.startsWith("#")) {
                    continue
                }
                //以?开头的属性。这种一般是使用了系统的属性 ?attr/xxx
                val resId = if (attributeValue.startsWith("?")) {
                    //去掉？
                    val attrId = attributeValue.substring(1).toInt()
                    //获取系统的属性id
                    SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
                } else {
                    //以@开头的属性。是开发者指定的值，一般在res/value文件下
                    //去掉@
                    attributeValue.substring(1).toInt()
                }
                val skinPair = SkinView.SkinPair(attributeName, resId)
                skinPairList.add(skinPair)
            }
        }
        if (skinPairList.isNotEmpty() || view is SkinViewSupport) {
            val skinView = SkinView(view, skinPairList)
            skinView.applySkin() //去加载皮肤
            mSkinViewList.add(skinView)
        }
    }

    /**
     * 更新所有的换肤
     */
    fun applySkin() {
        mSkinViewList.forEach {
            it.applySkin()
        }
    }

    data class SkinView(val view: View, val pairList: List<SkinPair>) {
        data class SkinPair(val attributeName: String, val resId: Int)

        //换肤
        fun applySkin() {
            if (view is SkinViewSupport) {
                view.applySkin()
            }
            var drawableTop: Drawable? = null
            var drawableLeft: Drawable? = null
            var drawableRight: Drawable? = null
            var drawableBottom: Drawable? = null
            pairList.forEach {
                when (it.attributeName) {
                    "textColor" -> {
                        if (view is TextView) {
                            view.setTextColor(SkinResourcesUtils.getColor(it.resId))
                        }
                    }
                    "background" -> {
                        val bg = SkinResourcesUtils.getBackground(it.resId)
                        if (bg is Int) {
                            view.setBackgroundColor(bg)
                        } else if (bg is Drawable) {
                            ViewCompat.setBackground(view, bg)
                        }
                    }

                    "src" -> {
                        val bg = SkinResourcesUtils.getBackground(it.resId)
                        if (view is ImageView) {
                            if (bg is Int) {
                                view.setImageDrawable(ColorDrawable(bg))
                            } else if (bg is Drawable) {
                                view.setImageDrawable(bg)
                            }
                        }
                    }

                    "drawableLeft" -> {
                        drawableLeft = SkinResourcesUtils.getDrawable(it.resId)
                    }

                    "drawableTop" -> {
                        drawableTop = SkinResourcesUtils.getDrawable(it.resId)
                    }

                    "drawableRight" -> {
                        drawableRight = SkinResourcesUtils.getDrawable(it.resId)
                    }

                    "drawableBottom" -> {
                        drawableBottom = SkinResourcesUtils.getDrawable(it.resId)
                    }
                }

                if (view is TextView && (drawableTop != null || drawableLeft != null || drawableRight != null || drawableBottom != null)) {
                    view.setCompoundDrawables(
                        drawableLeft,
                        drawableTop,
                        drawableRight,
                        drawableBottom
                    )
                }
            }
        }
    }

    companion object {
        //可以被换肤的属性集合
        val mAttributeNameList = arrayListOf(
            "textColor",
            "background",
            "src",
            "drawableLeft",
            "drawableTop",
            "drawableRight",
            "drawableBottom"
        )
    }
}