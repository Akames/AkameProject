package com.akame.akameproject

import android.app.Application
import com.akame.akameproject.classload.ClassLoaderUtils
import com.akame.skinlib.SkinManger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val apkPath = "${this.externalCacheDir?.path}/AkClassLoadTest.apk"
        ClassLoaderUtils.loadClass(apkPath)
        SkinManger.init(this)
    }
}