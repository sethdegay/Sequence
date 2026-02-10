package dev.sethdegay.buildlogic

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object SdkVersions {
    val PROJECT_SOURCE_COMPATIBILITY = JavaVersion.VERSION_11
    val PROJECT_TARGET_COMPATIBILITY = JavaVersion.VERSION_11
    val PROJECT_JVM_TARGET = JvmTarget.JVM_11

    const val APP_COMPILE_SDK = 36
    const val APP_MIN_SDK = 31

    const val LIBRARY_COMPILE_SDK = APP_COMPILE_SDK
    const val LIBRARY_MIN_SDK = APP_MIN_SDK
}