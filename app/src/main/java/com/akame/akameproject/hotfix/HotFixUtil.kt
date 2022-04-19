package com.akame.akameproject.hotfix

import android.content.Context
import android.util.Log
import java.io.File
import java.io.IOException
import java.lang.reflect.Array
import kotlin.collections.ArrayList

object HotFixUtil {

    fun hotFix(context: Context) {
        val fixDexPath = context.externalCacheDir!!.absolutePath + "/fixbug.dex"
        Log.e("tag", "-----fixDexPath------${fixDexPath}")
        val dexFile = File(fixDexPath)
        if (!dexFile.exists()) {
            return
        }
        val dexList = ArrayList<File>()
        dexList.add(dexFile)


        val clazz = Class.forName("dalvik.system.BaseDexClassLoader")
        val pathListFiled = clazz.getDeclaredField("pathList")
        pathListFiled.isAccessible = true
        val pathList = pathListFiled.get(context.classLoader)

        val pathListClazz = Class.forName("dalvik.system.DexPathList")
        val method = pathListClazz.getDeclaredMethod(
            "makeDexElements",
            List::class.java,
            File::class.java,
            List::class.java,
            ClassLoader::class.java
        )
        method.isAccessible = true
        val optimizedDirectory = File(context.cacheDir.absolutePath)
        val suppressedExceptions = java.util.ArrayList<IOException>()
        val fixBugEle = method.invoke(
            pathList,
            dexList,
            optimizedDirectory,
            suppressedExceptions,
            context.classLoader
        ) as kotlin.Array<*>
        Log.e("tag", "------获取到了FixBugEle------${fixBugEle.size}")

        val dexEleFiled = pathListClazz.getDeclaredField("dexElements")
        dexEleFiled.isAccessible = true
        val appDexEle = dexEleFiled.get(pathList) as kotlin.Array<*>
        Log.e("tag", "------获取到了AppDexEle------${appDexEle.size}")

        val newDexEle =
            Array.newInstance(appDexEle.javaClass.componentType, appDexEle.size + fixBugEle.size)
        System.arraycopy(fixBugEle, 0, newDexEle, 0, fixBugEle.size)
        System.arraycopy(appDexEle, 0, newDexEle, fixBugEle.size, appDexEle.size)

        dexEleFiled.set(pathList, newDexEle)
        Log.e("tag", "------热修复完成------")
    }
}