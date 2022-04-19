package com.akame.akameproject.hook

import android.os.Build
import kotlin.Throws
import android.content.Intent
import android.os.Handler
import com.akame.akameproject.hook.HookUtil2
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.Proxy

object HookUtil2 {
    private const val TARGET_INTENT = "target_intent"
    fun hookAMS() {
        try {
            // 获取 singleton 对象
            var singletonField: Field? = null
            singletonField = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // 小于8.0
                val clazz = Class.forName("android.app.ActivityManagerNative")
                clazz.getDeclaredField("gDefault")
            } else {
                val clazz = Class.forName("android.app.ActivityManager")
                clazz.getDeclaredField("IActivityManagerSingleton")
            }
            singletonField.isAccessible = true
            val singleton = singletonField[null]

            // 获取 系统的 IActivityManager 对象
            val singletonClass = Class.forName("android.util.Singleton")
            val mInstanceField = singletonClass.getDeclaredField("mInstance")
            mInstanceField.isAccessible = true
            val mInstance = mInstanceField[singleton]
            val iActivityManagerClass = Class.forName("android.app.IActivityManager")

            // 创建动态代理对象
            val proxyInstance = Proxy.newProxyInstance(
                Thread.currentThread().contextClassLoader,
                arrayOf(iActivityManagerClass)
            ) { proxy, method, args -> // do something
                // Intent的修改 -- 过滤
                /**
                 * IActivityManager类的方法
                 * startActivity(whoThread, who.getBasePackageName(), intent,
                 * intent.resolveTypeIfNeeded(who.getContentResolver()),
                 * token, target != null ? target.mEmbeddedID : null,
                 * requestCode, 0, null, options)
                 */
                /**
                 * IActivityManager类的方法
                 * startActivity(whoThread, who.getBasePackageName(), intent,
                 * intent.resolveTypeIfNeeded(who.getContentResolver()),
                 * token, target != null ? target.mEmbeddedID : null,
                 * requestCode, 0, null, options)
                 */
                // 过滤
                if ("startActivity" == method.name) {
                    var index = -1
                    for (i in args.indices) {
                        if (args[i] is Intent) {
                            index = i
                            break
                        }
                    }
                    // 启动插件的
                    val intent = args[index] as Intent
                    val proxyIntent = Intent()
                    proxyIntent.setClassName(
                        "com.enjoy.leo_plugin",
                        "com.enjoy.leo_plugin.ProxyActivity"
                    )
                    proxyIntent.putExtra(TARGET_INTENT, intent)
                    args[index] = proxyIntent
                }

                // args  method需要的参数  --- 不改变原有的执行流程
                // mInstance 系统的 IActivityManager 对象
                method.invoke(mInstance, *args)
            }

            // ActivityManager.getService() 替换成 proxyInstance
            mInstanceField[singleton] = proxyInstance
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hookHandler() {
        try {
            // 获取 ActivityThread 类的 Class 对象
            val clazz = Class.forName("android.app.ActivityThread")

            // 获取 ActivityThread 对象
            val activityThreadField = clazz.getDeclaredField("sCurrentActivityThread")
            activityThreadField.isAccessible = true
            val activityThread = activityThreadField[null]

            // 获取 mH 对象
            val mHField = clazz.getDeclaredField("mH")
            mHField.isAccessible = true
            val mH = mHField[activityThread] as Handler
            val mCallbackField = Handler::class.java.getDeclaredField("mCallback")
            mCallbackField.isAccessible = true

            // 创建的 callback
            val callback = Handler.Callback { msg -> // 通过msg  可以拿到 Intent，可以换回执行插件的Intent

                // 找到 Intent的方便替换的地方  --- 在这个类里面 ActivityClientRecord --- Intent intent 非静态
                // msg.obj == ActivityClientRecord
                when (msg.what) {
                    100 -> try {
                        val intentField = msg.obj.javaClass.getDeclaredField("intent")
                        intentField.isAccessible = true
                        // 启动代理Intent
                        val proxyIntent = intentField[msg.obj] as Intent
                        // 启动插件的 Intent
                        val intent = proxyIntent.getParcelableExtra<Intent>(TARGET_INTENT)
                        if (intent != null) {
                            intentField[msg.obj] = intent
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    159 -> try {
                        // 获取 mActivityCallbacks 对象
                        val mActivityCallbacksField = msg.obj.javaClass
                            .getDeclaredField("mActivityCallbacks")
                        mActivityCallbacksField.isAccessible = true
                        val mActivityCallbacks = mActivityCallbacksField[msg.obj] as List<*>
                        var i = 0
                        while (i < mActivityCallbacks.size) {
                            if (mActivityCallbacks[i]!!.javaClass.name
                                == "android.app.servertransaction.LaunchActivityItem"
                            ) {
                                val launchActivityItem = mActivityCallbacks[i]!!

                                // 获取启动代理的 Intent
                                val mIntentField = launchActivityItem.javaClass
                                    .getDeclaredField("mIntent")
                                mIntentField.isAccessible = true
                                val proxyIntent = mIntentField[launchActivityItem] as Intent

                                // 目标 intent 替换 proxyIntent
                                val intent = proxyIntent.getParcelableExtra<Intent>(TARGET_INTENT)
                                if (intent != null) {
                                    mIntentField[launchActivityItem] = intent
                                }
                            }
                            i++
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                // 必须 return false
                false
            }

            // 替换系统的 callBack
            mCallbackField[mH] = callback
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}