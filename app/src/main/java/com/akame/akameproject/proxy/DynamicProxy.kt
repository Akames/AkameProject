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
        //通过ProxyClassFactory#apply生成代理类。同时创建一个带有 InvocationHandler 的构造方法，可以把这个回调传入进去
        // 代理类会根据传入的class生成对应的方法。当代理类被调用的时候。InvocationHandler就会回调该方法信息调用信息
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