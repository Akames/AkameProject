package com.akame.akameproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.akame.ak_annotation.ARouter
import com.akame.akameproject.fish.FishActivity
import com.akame.skinlib.SkinManger
import java.lang.Exception

@ARouter(path = "app/MainActivity")
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        startActivity(Intent(this, AnnotationActivity::class.java))
//        val textView  = findViewById<TextView>(R.id.tv_test)
//        Log.e("tag","---->  className ${textView.javaClass.simpleName}")


        val testView = findViewById<TextView>(R.id.tv_test)
        testView.setOnClickListener {
//            val fileName = "file:///android_asset/skinapp.apk
//            val fileName = "${externalCacheDir?.path}/skinapp.apk"
//            Log.e("tag", "${File(fileName).exists()}")
            SkinManger.loadSkin(this.application, "${externalCacheDir?.path}/skinapp.apk")
        }
//
        val tvDefault = findViewById<TextView>(R.id.tv_default)
        tvDefault.setOnClickListener {
            SkinManger.loadSkin(this.application, "")
        }

//        testView.setOnClickListener {
//            startActivity(Intent(this, NestedActivity::class.java))
//        }

        val myTextView = findViewById<TextView>(R.id.tv_mytext)
        myTextView.setOnClickListener {
            startActivity(Intent(this, FishActivity::class.java))
        }

        try {
            val testLoaderClass = Class.forName("com.akame.skinapplication.AkClassLoadTest")
            val testClassLoader = testLoaderClass.newInstance()
            val testPrintMethod = testLoaderClass.getMethod("testPrint")
            testPrintMethod.invoke(testClassLoader)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}