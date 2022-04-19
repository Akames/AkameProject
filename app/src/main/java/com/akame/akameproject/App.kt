package com.akame.akameproject

import android.app.Application
import android.util.Log
import com.akame.akameproject.classload.ClassLoaderUtils
import com.akame.akameproject.hook.HookUtil
import com.akame.akameproject.hook.HookUtil2
import com.akame.akameproject.hotfix.HotFixUtil
import com.akame.skinlib.SkinManger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val apkPath = "/data/data/com.akame.akameproject/cache/testClassLoader.apk"
        ClassLoaderUtils.loadClass(apkPath)
        SkinManger.init(this)
        HookUtil.hookAMS()
        HotFixUtil.hotFix(this)
    }
}