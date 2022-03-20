package com.akame.akameproject.hook

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.akame.akameproject.R
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import javassist.ClassClassPath
import javassist.ClassPool

class HookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hook)

        //需要运行在xposed
//        XposedBridge.hookAllMethods(
//            TelephonyManager::class.java,
//            "getImei",
//            object : XC_MethodHook() {
//                override fun afterHookedMethod(param: MethodHookParam?) {
//                    super.afterHookedMethod(param)
//                    val hookName = param?.method?.name
//                    Log.e("tag", "-------${hookName}")
//                }
//            }
        changeTelephonyManger()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            getDeviceId()
//        }
        val test = MyHookSimple()
        test.test()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDeviceId() {
        val tm: TelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tm.imei
    }

    private fun changeTelephonyManger() {
//        val classPool = ClassPool.getDefault()
//        val cc = classPool.get("android.telephony.TelephonyManager")
//        val getImei = cc.getDeclaredMethod("getImei")
//        getImei.insertBefore("System.out.println(\"你笑起来真好看\")")



//        val classPool = ClassPool.getDefault()
//        classPool.insertClassPath(ClassClassPath(MyHookSimple::class.java))
//        val cc = classPool.get(MyHookSimple::class.java.name)
//        val getImei = cc.getDeclaredMethod("test")
//        getImei.insertBefore("System.out.println(\"你笑起来真好看\")")
    }


}