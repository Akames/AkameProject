package com.akame.akameproject.handler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.akame.akameproject.R
import kotlin.concurrent.thread

class HandlerActivity : AppCompatActivity() {
    /**
     * 内部类（匿名内部类）会有持有外部类的引用，所以 mHandler会持有Activity,
     * 这个时候发送message 会绑定当前handler然后在入队。
     * message入队后会编程MainLooper持有对象这样。这样通过可达性分析算法画。如果message在
     * 当前Activity生命周期前没有被处理掉。那么当前的Activity引用链会一直都在，导致外面的Activity也无法回收
     * 解决方案:通过静态内部类释放当前外部的类的引用可以达到效果
     */
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)
        thread {
            // 首先检查缓存池是否有处理过的message链表，如果有的话取出第一个，重置flags
            //通过Message.obtain创建message可以复用之前处理完的message对象。避免重复创建
            val handlerMessage = Message.obtain()
            mHandler.sendMessage(handlerMessage)
        }.start()
    }
}