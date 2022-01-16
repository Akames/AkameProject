package com.akame.akameproject.thread

import java.lang.Exception
import java.util.*


/**
 * 锁
 */
object LockThread {
    var falg = false
    val lock = Object()

    @JvmStatic
    fun main(args: Array<String>) {
        startProduce()
        startConsume()
    }

    private fun startProduce() {
        synchronized(lock) {
            Thread {
                for (i in 0..20) {
                    if (!falg) {
                        doProducer(i)
                        falg = true
                        try {
                            lock.notify()
                            lock.wait()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }.start()
        }
    }

    private fun startConsume() {
        synchronized(lock) {
            Thread {
                for (i in 0..20) {
                    if (falg){
                        doConsumer(i)
                        falg = false
                        try {
                            lock.notify()
                            lock.wait()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }.start()
        }
    }

    private fun doProducer(pos: Int) {
        println("生产商品：${pos}")
    }

    private fun doConsumer(pos: Int) {
        println("消费商品：${pos}")
    }
}