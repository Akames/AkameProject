package com.akame.akameproject

import android.app.Application
import com.akame.skinlib.SkinManger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SkinManger.init(this)
    }
}