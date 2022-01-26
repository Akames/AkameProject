package com.akame.akameproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.akame.akameproject.annotation.AnnotationActivity
import com.akame.akameproject.fish.FishActivity
import com.akame.akameproject.nested.NestedActivity
import com.akame.akameproject.text.MyTextView
import com.akame.akameproject.text.TextViewActivity
import com.akame.skinlib.SkinManger
import java.io.File

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
    }

    override fun onBackPressed() {
        super.onBackPressed()
        System.exit(0)
    }
}