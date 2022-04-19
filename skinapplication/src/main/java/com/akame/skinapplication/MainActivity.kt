package com.akame.skinapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("tag","------>> 我是插件化加载的")
//        setContentView(R.layout.activity_main)
        val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    }
}