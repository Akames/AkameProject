package com.akame.akameproject.classload

import android.content.Context
import android.util.Log
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File
import java.util.*

object ClassLoaderUtils {
    /**
     *@see dalvik.system.BaseDexClassLoader
     * @see dalvik.system.DexPathList
     */
    fun loadClass(loadApkPath: String) {
        Log.e("Tag", "---loadPath ${loadApkPath}")
        val file = File(loadApkPath)
        if (!file.exists()) {
            Log.e("tag", "文件不存在")
            return
        }
        //反射获取 PathList
        val dexClassLoader = Class.forName("dalvik.system.BaseDexClassLoader")
        val pathListField = dexClassLoader.getDeclaredField("pathList")
        pathListField.isAccessible = true

        //反射获取 dexElements
        val dexPathList = Class.forName("dalvik.system.DexPathList")
        val dexElementsField = dexPathList.getDeclaredField("dexElements")
        dexElementsField.isAccessible = true

        //获取到了pathList的值
        val pathList = pathListField.get(this::class.java.classLoader)
        Log.e("Tag", "----->> ${pathList}")
        //得到dexElements的值
        val dexElements = dexElementsField.get(pathList) as Array<*>

        //拿到apk的DexElements
        val apkClassLoader = DexClassLoader(loadApkPath, "", "", this::class.java.classLoader)
        val apkPathList = pathListField.get(apkClassLoader)
        //拿到插件apk的dexElements
        val apkDexElements = dexElementsField.get(apkPathList) as Array<*>
        Log.e("Tag", "----->> ${apkDexElements.size}")

        //因为dexElements是一个数组 可以通过下面的代码返回数组类型
        val dexType = dexElements.javaClass.componentType
        val newDexElements = java.lang.reflect.Array.newInstance(
            dexType, dexElements.size + apkDexElements.size
        )
        System.arraycopy(dexElements, 0, newDexElements, 0, dexElements.size)
        System.arraycopy(apkDexElements, 0, newDexElements, dexElements.size, apkDexElements.size)
        dexElementsField.set(pathList, newDexElements)
    }
}