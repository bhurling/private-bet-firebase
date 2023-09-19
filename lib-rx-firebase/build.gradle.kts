@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = rootProject.ext["compileSdk"] as Int

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    api(Config.Libs.Kotlin.STDLIB)

    api(Config.Libs.Firebase.DATABASE)

    api(Config.Libs.Rx.JAVA)
}
