package com.akame.akameproject.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InjectClick(vararg val value: Int)
