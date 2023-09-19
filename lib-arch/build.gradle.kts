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
}

dependencies {
    api(libs.rx.java)
    api(libs.androidx.annotation)

    implementation(libs.mvrx)
    implementation(libs.rx.kotlin)

    implementation(libs.kotlin.stdlib)
}
