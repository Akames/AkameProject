package com.akame.proguardfiles

import android.util.Log
import java.io.File
import kotlin.concurrent.thread

object MyTest {
    fun test() {
        Log.e("Tag","-----")
        thread {
            Thread.sleep(2000)
            System.out.println("1111")
        }.start()
    }
}