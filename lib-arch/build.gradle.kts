@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = rootProject.ext["compileSdk"] as Int

    defaultConfig {
        namespace = "io.bhurling.privatebet.arch"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    api(libs.rx.java)
    api(libs.androidx.annotation)

    implementation(libs.rx.kotlin)

    implementation(libs.kotlin.stdlib)
}
