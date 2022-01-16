package com.akame.skinlib

import android.content.Context
import android.util.AttributeSet
import android.view.View
import java.lang.reflect.Constructor
import java.util.*

/**
 * 创建view
 */
internal object SkinCrateViewManger {
    /**
     * 所有创建view的构造器容器
     */
    private val sConstructorMap = HashMap<String, Constructor<out View?>>()

    /**
     * 指定调用那个构造器
     */
    private val mConstructorSignature = arrayOf(
        Context::class.java, AttributeSet::class.java
    )

    /**
     * 系统view的名字拼接 比如LinearLayout 就需要拼接成全路径
     */
    private val sClassPrefixList = arrayOf(
        "android.widget.",
        "android.webkit.",
        "android.app.",
        "android.view."
    )

    /**
     * 创建view
     * @param viewName xml解析的view的名字
     * @param attrs xml解析view的属性
     */
    fun crateView(context: Context, viewName: String, attrs: AttributeSet): View? {
        //如果view的名字中不包含. 说名是系统的view，所以需要拼接成全路径
        if (-1 == viewName.indexOf('.')) {
            for (prefix in sClassPrefixList) {
                //遍历拼接全路径 直到创建view为止
                val view = createView(context, viewName, prefix, attrs)
                if (view != null) {
                    return view
                }
            }
            //如果全部都没有找到只能返回null了交给系统去创建。或者这个view根本就不存在
            return null
        } else {
            //如果是全路径的view则直接去创建
            return createView(context, viewName, null, attrs)
        }
    }

    private fun createView(
        context: Context,
        name: String,
        prefix: String?,
        attrs: AttributeSet
    ): View? {
        //拼接名字
        val viewName = if (prefix != null) prefix + name else name
        //找到view的构造器
        val viewConstructor = findConstructor(context, viewName)
        //反射创建view
        return viewConstructor?.newInstance(context, attrs)
    }

    private fun findConstructor(context: Context, viewName: String): Constructor<out View?>? {
        //首先去缓存中找是否之前创建过
        var constructor: Constructor<out View?>? = sConstructorMap[viewName]
        try {
            //如果没有再去反射创建
            if (constructor == null) {
                val clazz = Class.forName(
                    viewName, false, context.classLoader
                ).asSubclass(View::class.java)
                constructor = clazz.getConstructor(*mConstructorSignature)
                constructor.isAccessible = true
                //保存到缓存
                sConstructorMap[viewName] = constructor
            }
        } catch (e: Exception) {

        }
        return constructor
    }
}