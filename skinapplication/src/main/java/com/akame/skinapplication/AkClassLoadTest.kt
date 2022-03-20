package com.akame.skinapplication

import android.util.Log

class AkClassLoadTest {
    fun testPrint() {
        Log.e("tag", "--------->> 我是从插件的类")
    }

    private fun testPrint2() {
        Log.e("tag", "--------->> 我也是插件得类，但是我是私有的")
    }

    private fun testPrint3(name: String): String {
        Log.e("tag", "--------->> 我也是插件的 我还有参数： ${name}")
        return name + "携带密码"
    }
}