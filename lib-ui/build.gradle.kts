@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    defaultConfig {
        namespace = "io.bhurling.privatebet.ui"
    }

    compileSdk = rootProject.ext["compileSdk"] as Int
}

dependencies {
    api(libs.kotlin.stdlib)

    api(libs.androidx.core)
    api(libs.androidx.recyclerview)
}
