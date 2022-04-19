package com.akame.akameproject.hook

import android.annotation.SuppressLint
import android.util.Log
import java.lang.reflect.Proxy

object HookUtil {
    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    fun hookAMS() {
        val activityTaskManagerCls = Class.forName("android.app.ActivityManager")
        val taskManagerFiled =
            activityTaskManagerCls.getDeclaredField("IActivityManagerSingleton")
        taskManagerFiled.isAccessible = true
        val singleton = taskManagerFiled.get(null)

        val singletonCls = Class.forName("android.util.Singleton")
        val mInstanceField = singletonCls.getDeclaredField("mInstance")
        mInstanceField.isAccessible = true
//        val getMethod = singletonCls.getDeclaredMethod("get")
//        val mInstance = getMethod.invoke(taskManager)
        val mInstance = mInstanceField.get(singleton)
        Log.e("tag", "----> ${mInstance}")

        val iActivityTaskMangerClass = Class.forName("android.app.IActivityManager")
        val proxyInstance = Proxy.newProxyInstance(
            Thread.currentThread().contextClassLoader,
            arrayOf<Class<*>>(iActivityTaskMangerClass)
        ) { proxy, method, args ->
            method?.invoke(mInstance, *args)
        }

        mInstanceField.set(singleton, proxyInstance)
    }
}