@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    defaultConfig {
        namespace = "io.bhurling.privatebet.rx.firebase"
    }
    
    compileSdk = rootProject.ext["compileSdk"] as Int

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    api(libs.kotlin.stdlib)

    api(libs.firebase.database)

    api(libs.rx.java)
}
