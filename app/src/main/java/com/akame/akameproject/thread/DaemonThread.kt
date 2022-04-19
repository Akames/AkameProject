package com.akame.akameproject.thread

import kotlin.concurrent.thread
import kotlin.system.exitProcess

object DaemonThread {
    @JvmStatic
    fun main(args: Array<String>) {
        val thread = Thread(Runnable {
            try {
                Thread.sleep(3000)
                println("子线程退出")
            }catch (e:Exception){
                e.printStackTrace()
            }finally {
               println("finally")
            }
        })
        thread.isDaemon = true
        thread.start()
        Thread.sleep(1000)
        println("主线程退出")
        thread.join()
    }
}