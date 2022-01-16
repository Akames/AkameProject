package com.akame.akameproject.decorate

import android.util.Log

class DecorateDe1(abs: AbsDecorate) : DecorateImpl() {
    init {
        this.abs = abs
    }

    override fun testAbs() {
        super.testAbs() //首先执行父类的方法
        abs.testAbs() //在执行兄弟的同方法
        println("DecorateDe1") //最后在执行自己的方法
    }
}