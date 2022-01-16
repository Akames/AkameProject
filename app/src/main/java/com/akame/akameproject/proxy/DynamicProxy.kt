package com.akame.akameproject.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 动态代理
 */
object DynamicProxy {
    @JvmStatic
    fun main(args: Array<String>) {

        val test = Test()
        val proxy = Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(IProxy::class.java)
        ) { proxy, method, args ->
            println("代理前 ${args?.size}")
            method.invoke(test)
            println("代理后")
        }
        (proxy as IProxy).doSomething()
    }

    interface IProxy {
        fun doSomething()
    }

    class Test : IProxy {
        override fun doSomething() {
            println("我在干活")
        }
    }
}