package com.akame.akameproject.decorate

abstract class DecorateImpl : AbsDecorate() {
    lateinit var abs: AbsDecorate

    override fun testAbs() {
        println("DecorateImpl")
    }
}