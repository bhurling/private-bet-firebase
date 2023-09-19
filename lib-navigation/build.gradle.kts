@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = rootProject.ext["compileSdk"] as Int
}

dependencies {
    api(Config.Libs.Kotlin.STDLIB)
}
