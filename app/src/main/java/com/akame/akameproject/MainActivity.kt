package com.akame.akameproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.akame.ak_annotation.ARouter
import com.akame.akameproject.hotfix.HotFixActivity
import com.akame.akameproject.nested.NestedActivity
import com.akame.skinlib.SkinManger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET

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

        testView.setOnClickListener {
            startActivity(Intent(this, NestedActivity::class.java))
        }

        val myTextView = findViewById<TextView>(R.id.tv_mytext)
        myTextView.setOnClickListener {
            startActivity(Intent(this, HotFixActivity::class.java))
        }

        //从其他apk加载类 通过类加载器将apk的dex文件加载到项目类加载器的dex文件中
        try {
            val testLoaderClass = Class.forName("com.akame.skinapplication.AkClassLoadTest")
            val testClass = testLoaderClass.newInstance()
            val testPrintMethod = testLoaderClass.getMethod("testPrint")
            testPrintMethod.invoke(testClass)

            val testPrint2 = testLoaderClass.getDeclaredMethod("testPrint2")
            testPrint2.isAccessible = true
            testPrint2.invoke(testClass)

            val testPrint3 = testLoaderClass.getDeclaredMethod("testPrint3", String::class.java)
            testPrint3.isAccessible = true
            val result = testPrint3.invoke(testClass, "嘿嘿")
            Log.e("tag", "---->> 拿到插件返回值 ${result}")

        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }


    }


    private fun testNetwork() {
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .build()

        val api = retrofit.create(Api::class.java)
        api.test().enqueue(object : Callback<Response<String>>{
            override fun onResponse(p0: Call<Response<String>>, p1: Response<Response<String>>) {

            }

            override fun onFailure(p0: Call<Response<String>>, p1: Throwable) {
            }
        })
    }

    interface Api {
        @GET("")
        fun test(): Call<Response<String>>
    }
}