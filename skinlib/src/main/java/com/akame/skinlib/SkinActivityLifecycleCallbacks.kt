package com.akame.skinlib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import java.util.*

internal class SkinActivityLifecycleCallbacks(private val mObservable: Observable) :
    Application.ActivityLifecycleCallbacks {
    private val factoryMap = hashMapOf<Activity, SkinLayoutInflaterFactory>()
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //反射设置我们自定义的skinFactory
        val layoutInflater = activity.layoutInflater
        val skinLayoutInflaterFactory = SkinLayoutInflaterFactory(activity)
        val declaredField = LayoutInflater::class.java.getDeclaredField("mFactory2")
        declaredField.isAccessible = true
        declaredField.set(layoutInflater, skinLayoutInflaterFactory)

        val declaredField2 = LayoutInflater::class.java.getDeclaredField("mFactory")
        declaredField2.isAccessible = true
        declaredField2.set(layoutInflater, skinLayoutInflaterFactory)
        //将Factory与Activity进行关联，通过map管理。
        factoryMap[activity] = skinLayoutInflaterFactory
        //绑定观察者
        mObservable.addObserver(skinLayoutInflaterFactory)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        //页面销毁的时候 解除绑定
        factoryMap.remove(activity).let {
            mObservable.deleteObserver(it)
        }
    }
}