package com.akame.akameproject.annotation

import android.view.View
import androidx.appcompat.app.AppCompatActivity

object AnnotationClickManager {
    fun inject(activity: AppCompatActivity) {
        val cls = activity.javaClass
        cls.methods.forEach {
            if (it.isAnnotationPresent(InjectClick::class.java)) {
                val annotation = it.getAnnotation(InjectClick::class.java) as InjectClick
                val ids = annotation.value
                ids.forEach { id ->
                    activity.findViewById<View>(id)?.setOnClickListener { view ->
                        it.invoke(activity, view)
                    }
                }
            }
        }
    }
}