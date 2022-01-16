package com.akame.skinlib

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import java.util.*

/**
 * 布局解析器，通过设置解析器可以拦截view生成，并解析属性
 */
internal class SkinLayoutInflaterFactory(private val activity: Activity) : LayoutInflater.Factory2,
    Observer {
    private val skinAttribute by lazy { SkinAttribute() }
    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        val view = SkinCrateViewManger.crateView(context, name, attrs)
        if (view != null) {
            //如果成功创建出view则去检查view的属性是否存在换肤的属性
            skinAttribute.look(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    /**
     * 接收到了观察的更新请求。进行皮肤更新
     */
    override fun update(o: Observable?, arg: Any?) {
        //通知系统更新状态栏
        SkinThemeUtils.updateStatusBarColor(activity)
        skinAttribute.applySkin()
    }
}