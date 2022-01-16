package com.akame.akameproject.thread

import java.util.concurrent.locks.ReentrantLock

/**
 * 使用可重入锁（显示锁）的案例
 */
object ReentrantLockSimple {
    private val lock = ReentrantLock()

    @JvmStatic
    fun main(args: Array<String>) {

        val t1 = Thread {
            doSomething()
        }

        val t2 = Thread {
            doSomething()
        }

        val t3 = Thread {
            doSomething()
        }
        t1.start()
        t2.start()
        t3.start()
        t1.interrupt()
        t1.isInterrupted
    }

    private fun doSomething() {
        lock.lock()
        println("线程${Thread.currentThread().name}正在执行")
        lock.unlock()
    }

}