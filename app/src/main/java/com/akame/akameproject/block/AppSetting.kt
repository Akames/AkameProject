package com.akame.akameproject.block

import android.os.Debug
import android.os.StrictMode

class AppSetting {

    /**
     * 设置App启动严格检测模式
     */
    fun setAppStrictMode() {
        //设置线程检查
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectAll()
                .penaltyLog() //打印违规日志
                .penaltyDeath()  //违规崩溃
                .build()
        )

        //设置jvm检查 比如应该释放的对象没有释放就会崩溃
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    /**
     * 检测app类内存片段
     */
    fun checkAppMemory() {
        //开启  需要开启存储权限 -> 会将收集的结果存在手机根目录leack下
        Debug.startMethodTracing("leack")
        //结束
        Debug.stopMethodTracing()
    }
}