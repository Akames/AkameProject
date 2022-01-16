package com.akame.akameproject.proxy

/**
 * 静态代理
 */
object StaticProxy {
    @JvmStatic
    fun main(args: Array<String>) {
        val pr = ProxyComp()
        pr.showMsg("我要输出")
    }

    interface IProxy {
        fun showMsg(msg: String)
    }

    class Test : IProxy {
        override fun showMsg(msg: String) {
            println("小何在输出消息 ：${msg}")
        }
    }

    class ProxyComp : IProxy {
        val test = Test()
        override fun showMsg(msg: String) {
            println("代理开始")
            test.showMsg(msg)
            println("代理结束")
        }
    }
}