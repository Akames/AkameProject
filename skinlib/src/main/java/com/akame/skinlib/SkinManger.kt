package com.akame.skinlib

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import java.util.*

/**
 * 换肤管理 调用入口
 */
object SkinManger : Observable() {
    /**
     * 初始化
     */
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(SkinActivityLifecycleCallbacks(this))
        SkinDataCache.init(application)
        val skinPath = SkinDataCache.getSkinPath()
        loadSkin(application, skinPath)
    }

    /**
     * 加载皮肤
     */
    fun loadSkin(context: Application, skinPath: String) {
        if (skinPath.isEmpty()) {
            //如果皮肤包路径为空，则使用默认皮肤
            SkinResourcesUtils.rest()
            SkinDataCache.reset()
            SkinResourcesUtils.apply(context.resources, null, null)
        } else {
            //获取默认的皮肤包
            val appResources = context.resources
            //反射长江assetManger
            val skinAssetManger = AssetManager::class.java.newInstance()
            val addAssetPathMethod =
                skinAssetManger.javaClass.getMethod("addAssetPath", String::class.java)
            //反射设置皮肤包路径给assetManger
            addAssetPathMethod.invoke(skinAssetManger, skinPath)
            //创建SkinResources
            val skinResources =
                Resources(skinAssetManger, appResources.displayMetrics, appResources.configuration)
            //获取包管理器
            val mpm = context.packageManager
            val info = mpm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            val skinPackageName = info?.packageName //获取皮肤包的包名
            SkinResourcesUtils.apply(appResources, skinResources, skinPackageName)
            SkinDataCache.setSkinPath(skinPath)
        }
        //更新系统
        //通知observer更新
        setChanged()
        notifyObservers()
    }
}