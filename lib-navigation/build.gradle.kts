@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    defaultConfig {
        namespace = "io.bhurling.privatebet.navigation"
    }
    compileSdk = rootProject.ext["compileSdk"] as Int
}

dependencies {
    api(libs.kotlin.stdlib)
}
