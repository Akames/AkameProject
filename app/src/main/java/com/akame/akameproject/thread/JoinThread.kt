package com.akame.akameproject.thread

import kotlin.concurrent.thread

object JoinThread {
    @JvmStatic
    fun main(args: Array<String>) {
        val thread1 = Thread {
            for (i in 0..10) {
                if (Thread.interrupted()) {
                    return@Thread
                }
                println(Thread.currentThread().name + "**$i")
            }
        }

        val thread2 = Thread {
            for (i in 0..10) {
                println(Thread.currentThread().name + "**$i")
            }
        }

        val thread3 = Thread {
            for (i in 0..10) {
                println(Thread.currentThread().name + "**$i")
            }
        }

        thread1.start()
        thread1.join() //通过join可以控制线程的执行顺序 否则就是线程并行状态，3个线程等待CPU轮转执行
        thread2.start()
        thread2.join()
        thread3.start()

//        thread1.interrupt() // 通知线程结束
//        thread1.isDaemon = true //设置线程守护 当启动线程执行完毕，不管子线程是否执行完毕都必须结束。
    }
}