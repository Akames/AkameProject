package com.akame.akameproject.decorate

/**
 * 装饰模式 让把子类关联起来，让子类可以随意搭配
 */
object DecorateDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val de1 = DecorateDe1(
            DecorateDe2(object :AbsDecorate(){
                override fun testAbs() {

                }
            })
        )
        de1.testAbs()
    }
}

